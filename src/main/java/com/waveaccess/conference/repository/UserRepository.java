package com.waveaccess.conference.repository;

import com.waveaccess.conference.config.ApplicationRole;
import com.waveaccess.conference.dto.UserDto;
import com.waveaccess.conference.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findAllByLogin(String login);

    @Transactional
    @Modifying
    @Query("update User u set u.role = :newRole where u.login = :newLogin")
    Integer update(String newLogin, ApplicationRole newRole);

    @Query("select new com.waveaccess.conference.dto.UserDto(u.login, " +
            "u.isAccountNonExpired, " +
            "u.isAccountNonLocked, " +
            "u.isCredentialsNonExpired, " +
            "u.isEnabled, " +
            "u.role) " +
            "from User u where u.role = :role")
    List<UserDto> findAllByRole(ApplicationRole role);

    @Transactional
    @Modifying
    @Query("delete from User u where u.login = :login")
    Integer deleteByLogin(String login);
}
