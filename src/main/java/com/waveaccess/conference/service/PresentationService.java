package com.waveaccess.conference.service;

import com.waveaccess.conference.dto.PresentationDto;
import com.waveaccess.conference.entity.*;
import com.waveaccess.conference.exception.TimePresentationsException;
import com.waveaccess.conference.repository.RoomRepository;
import com.waveaccess.conference.repository.UserRepository;
import com.waveaccess.conference.repository.PresentationRepository;
import com.waveaccess.conference.repository.PresentationUserRepository;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
public class PresentationService {

    private final PresentationRepository presentationRepository;
    private final PresentationUserRepository presentationUserRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public PresentationService(PresentationRepository presentationRepository, PresentationUserRepository presentationUserRepository, RoomRepository roomRepository, UserRepository userRepository) {
        this.presentationRepository = presentationRepository;
        this.presentationUserRepository = presentationUserRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    public Map<String, List<Presentation>> getSchedule(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        List<Presentation> presentations = presentationRepository.findOnThisDate(date, calendar.getTime());
        return presentations.stream()
                .collect(groupingBy(i -> i.getSchedule().getRoom().getAudience(), toList()));
    }

    public List<PresentationDto> getUserPresentations(String login) {
        return presentationUserRepository.findAllByLogin(login).stream()
                .map(i -> convertPresentationToDto(i.getPresentation().getPresentation())).collect(Collectors.toList());
    }

    private PresentationDto convertPresentationToDto(Presentation presentation) {
        return new PresentationDto(presentation.getId(),
                presentation.getName(),
                presentation.getSchedule().getDateTimeStart(),
                presentation.getSchedule().getDateTimeEnd(),
                presentation.getSchedule().getRoom().getAudience()
        );
    }

    public Presentation createPresentation(PresentationDto presentationDto, String login) {
        List<Presentation> presentationsInThisPeriodOfTime = findPresentationsInThisPeriodOfTime(presentationDto);

        if (presentationsInThisPeriodOfTime.isEmpty()) {
            Room room = getRoom(presentationDto.getAudience());
            User user = userRepository.findAllByLogin(login);
            Presentation presentation = createPresentationObject(room, presentationDto);
            subscribeToPresentation(presentationRepository.save(presentation), user, PresentationRole.Presenter);
            return presentation;
        } else {
            throw new TimePresentationsException("time is busy", presentationsInThisPeriodOfTime);
        }

    }

    public PresentationDto subscribeUserToPresentation(String login, PresentationDto presentationDto) {
        User user = userRepository.findAllByLogin(login);
        Presentation presentation = presentationRepository.findById(presentationDto.getId()).orElse(null);
        return convertPresentationToDto(subscribeToPresentation(presentation, user, PresentationRole.Listener).getPresentation());
    }

    public PresentationEmbeddable subscribeToPresentation(Presentation presentation, User user, PresentationRole presentationRole) {
        PresentationEmbeddable presentationEmbeddable = new PresentationEmbeddable(presentation, user);
        PresentationUser presentationUser = new PresentationUser(
                presentationEmbeddable,
                presentationRole
        );
        PresentationUser save = presentationUserRepository.save(presentationUser);
        return save.getPresentation();
    }

    public List<Presentation> findPresentationsInThisPeriodOfTime(PresentationDto presentationDto) {
        return presentationRepository.findPresentationsBetweenTwoDate(
                presentationDto.getDateTimeStartPresentation(),
                presentationDto.getDateTimeEndPresentation(),
                presentationDto.getAudience());
    }

    public Presentation createPresentationObject(Room room, PresentationDto presentationDto) {
        Schedule schedule = new Schedule(
                null,
                room,
                presentationDto.getDateTimeStartPresentation(),
                presentationDto.getDateTimeEndPresentation()
        );

        return new Presentation(null, presentationDto.getName(), schedule);
    }

    public Room getRoom (String audience) {
        Room room = roomRepository.findByAudience(audience);
        if (room == null) {
            throw new IllegalArgumentException("No found audience " + audience);
        } else {
            return roomRepository.findByAudience(audience);
        }
    }


    public Integer deletePresentation(PresentationDto presentationDto) {
        Presentation presentation = presentationRepository.findById(presentationDto.getId()).orElse(null);
        if (presentation == null) {
            throw new IllegalArgumentException("No found presentation by id");
        }
        Integer delete = presentationUserRepository.deleteByPresentationId(presentationDto.getId());
        presentationRepository.delete(presentation);
        return delete;
    }

    public PresentationDto editPresentation(PresentationDto presentationDto, String login) {

        Presentation presentation = presentationRepository.findById(presentationDto.getId()).orElse(null);
        if (presentation == null) {
            throw new IllegalArgumentException("No found presentation by id");
        }

        boolean checkOwnerPresentation = checkOwnerPresentation(presentationDto.getId(), login);
        if (!checkOwnerPresentation) {
            throw new IllegalArgumentException("You not owner presentation");
        }
        List<Presentation> presentationsInThisPeriodOfTime = findPresentationsInThisPeriodOfTime(presentationDto)
                .stream().filter(i -> !i.getId().equals(presentationDto.getId())).collect(toList());
        if (!presentationsInThisPeriodOfTime.isEmpty()) {
            throw new TimePresentationsException("time is busy other presentation", presentationsInThisPeriodOfTime);
        } else {
            presentation.getSchedule().setRoom(getRoom(presentationDto.getAudience()));
            presentation.setName(presentationDto.getName());
            presentation.getSchedule().setDateTimeStart(presentationDto.getDateTimeStartPresentation());
            presentation.getSchedule().setDateTimeEnd(presentationDto.getDateTimeEndPresentation());
            Presentation editPresentation = presentationRepository.save(presentation);
            return convertPresentationToDto(editPresentation);
        }
    }

    private boolean checkOwnerPresentation(Long idPresentation, String userLogin) {
        long count = getUserPresentations(userLogin).stream().filter(i -> i.getId().equals(idPresentation)).count();
        return count >= 1;
    }
}
