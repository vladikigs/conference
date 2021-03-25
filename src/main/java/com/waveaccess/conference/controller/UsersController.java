package com.waveaccess.conference.controller;

import com.waveaccess.conference.config.ApplicationRole;
import com.waveaccess.conference.dto.UserDto;
import com.waveaccess.conference.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("user")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    @ResponseBody
    public UserDto registerNewUser(UserDto userDto) {
        userDto.setRole(ApplicationRole.User);
        return userService.registerUser(userDto);
    }

    @GetMapping("/show-users")
    @PreAuthorize("hasRole('Admin')")
    @ResponseBody
    public java.util.List<UserDto> showUsers(UserDto userDto) {
        return userService.showUsers(userDto);
    }

    @PostMapping("/add-user")
    @PreAuthorize("hasRole('Admin')")
    @ResponseBody
    public UserDto addUser(UserDto userDto) {
        return userService.registerUser(userDto);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('Admin')")
    @ResponseBody
    public Integer updateUser(UserDto user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('Admin')")
    @ResponseBody
    public Integer deleteUser(UserDto user) {
        return userService.delete(user);
    }

}
