package edu.lnu.musicly.streaming.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import edu.lnu.musicly.streaming.dto.UserDto;

public interface ApplicationUserService extends UserDetailsService {
    void saveUser(UserDto userDto);

    UserDto findUserByUsername(String username);
}
