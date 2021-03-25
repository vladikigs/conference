package com.waveaccess.conference.controller;

import com.waveaccess.conference.dto.PresentationDto;
import com.waveaccess.conference.entity.Presentation;
import com.waveaccess.conference.service.PresentationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("presentation")
public class PresentationController {


    private final PresentationService presentationService;

    public PresentationController(PresentationService presentationService) {
        this.presentationService = presentationService;
    }

    @GetMapping("/schedule")
    @ResponseBody
    public Map<String, List<Presentation>> schedulePresentation(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        return presentationService.getSchedule(date);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('Presenter')")
    @ResponseBody
    public Presentation createPresentation(PresentationDto presentationDto, Authentication authentication) {
        return presentationService.createPresentation(presentationDto, authentication.getName());
    }

    @GetMapping("/my-presentations")
    @PreAuthorize("hasRole('Presenter')")
    @ResponseBody
    public List<PresentationDto> registrationForThePresentation(Authentication authentication) {
        return presentationService.getUserPresentations(authentication.getName());
    }

    @PutMapping("/edit-presentation")
    @PreAuthorize("hasRole('Presenter')")
    @ResponseBody
    public PresentationDto editPresentation(PresentationDto presentationDto, Authentication authentication) {
        return presentationService.editPresentation(presentationDto, authentication.getName());
    }

    @DeleteMapping("/delete-my-presentation")
    @PreAuthorize("hasRole('Presenter')")
    @ResponseBody
    public Integer deletePresentation(PresentationDto presentationDto) {
        return presentationService.deletePresentation(presentationDto);
    }

    @PostMapping("/subscribe-to-presentation")
    @ResponseBody
    public PresentationDto subscribeToPresentation(Authentication authentication, PresentationDto presentationDto) {
        return presentationService.subscribeUserToPresentation(authentication.getName(), presentationDto);
    }

}
