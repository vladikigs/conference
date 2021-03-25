package com.waveaccess.conference.repository;

import com.waveaccess.conference.entity.Presentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Date;
import java.util.List;

public interface PresentationRepository extends JpaRepository<Presentation, Long> {


    @Query("select h from Presentation h WHERE (h.schedule.dateTimeStart between ?1 AND ?2 OR h.schedule.dateTimeEnd between ?1 AND ?2) AND h.schedule.room.audience like ?3")
    List<Presentation> findPresentationsBetweenTwoDate(Date timeStartPresentation, Date timeEndPresentation, String audience);


    @Query("select h from Presentation h WHERE (h.schedule.dateTimeStart between ?1 AND ?2)")
    List<Presentation> findOnThisDate(Date date, Date datePlusOneDay);

}