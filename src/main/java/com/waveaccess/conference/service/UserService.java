package com.waveaccess.conference.service;

import com.waveaccess.conference.dto.UserDto;
import com.waveaccess.conference.entity.User;
import com.waveaccess.conference.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    public Integer updateUser(UserDto userDto) {
        return userRepository.update(userDto.getLogin(), userDto.getRole());
    }

    @Override
    public User loadUserByUsername(String login) {
        return userRepository.findAllByLogin(login);
    }

    public UserDto registerUser(UserDto userDto) {
        User user = new User(
                null,
                userDto.getLogin(),
                passwordEncoder.encode(userDto.getPassword()),
                true,
                true,
                true,
                true,
                userDto.getRole()
        );
        return convertToDto(userRepository.save(user));
    }


    public List<UserDto> showUsers(UserDto userDto) {
        if (userDto.getLogin() == null) {
            return userRepository.findAllByRole(userDto.getRole());
        } else {
            ArrayList<UserDto> oneUser = new ArrayList<>();
            oneUser.add(convertToDto(userRepository.findAllByLogin(userDto.getLogin())));
            return oneUser;
        }

    }

    public Integer delete(UserDto user) {
        return userRepository.deleteByLogin(user.getLogin());
    }

    private UserDto convertToDto(User user) {
        return new UserDto(
                user.getUsername(),
                user.isAccountNonExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired(),
                user.isEnabled(),
                user.getRole());
    }

}
