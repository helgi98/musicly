package edu.lnu.musicly.streaming.controllers;

import edu.lnu.musicly.streaming.dto.UserDto;
import edu.lnu.musicly.streaming.services.ApplicationUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

import static edu.lnu.musicly.streaming.filters.constants.SecurityConstants.SIGN_UP_URL;


@RestController
public class UserController {

    private final ApplicationUserService userService;

    public UserController(ApplicationUserService userService) {
        this.userService = userService;
    }

    @PostMapping(SIGN_UP_URL)
    public void signUp(@RequestBody @Valid UserDto user) {
        userService.saveUser(user);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/users/info")
    public UserDto info(Principal principal) {
        return userService.findUserByUsername(principal.getName());
    }
}
