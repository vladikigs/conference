package com.waveaccess.conference.repository;

import com.waveaccess.conference.entity.PresentationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PresentationUserRepository extends JpaRepository<PresentationUser, Long> {

    @Query("select pu " +
           "from PresentationUser pu where pu.presentation.user.login = :login")
    List<PresentationUser> findAllByLogin(String login);

    @Transactional
    @Modifying
    @Query("delete from PresentationUser pu where pu.presentation.presentation.id = :id")
    Integer deleteByPresentationId(Long id);


}
