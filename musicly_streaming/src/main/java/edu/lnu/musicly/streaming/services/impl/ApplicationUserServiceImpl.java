package edu.lnu.musicly.streaming.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.lnu.musicly.streaming.dao.ApplicationUserDao;
import edu.lnu.musicly.streaming.dto.UserDto;
import edu.lnu.musicly.streaming.entities.ApplicationUser;
import edu.lnu.musicly.streaming.entities.Role;
import edu.lnu.musicly.streaming.exceptions.ValidationException;
import edu.lnu.musicly.streaming.services.ApplicationUserService;

import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;
import static edu.lnu.musicly.streaming.dto.UserDto.from;
import static edu.lnu.musicly.streaming.dto.UserDto.to;
import static edu.lnu.musicly.streaming.utils.CollectionUtils.mapToList;

@Slf4j
@Service
public class ApplicationUserServiceImpl implements ApplicationUserService {
    public final String APP_USER = "APP";

    private final ApplicationUserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationUserServiceImpl(ApplicationUserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) {
        log.debug("Loading user {}", username);
        ApplicationUser user = userDao.findByUsername(username).orElse(null);

        if (isNull(user) || user.getRoles().isEmpty()) {
            log.debug("No user for username {} found", username);
            throw new UsernameNotFoundException("No user found");
        }

        return new User(user.getUsername(), user.getPassword(), mapToList(user.getRoles(),
                r -> new SimpleGrantedAuthority(r.getRole())));
    }

    @Transactional
    public void saveUser(UserDto userDto) {
        log.debug("Saving user. Username {}, email {}", userDto.getUsername(), userDto.getEmail());

        if (!userDao.isUnique(userDto.getUsername(), userDto.getEmail())) {
            log.debug("User with such username {} / email {} already exists", userDto.getUsername(), userDto.getEmail());
            throw new ValidationException("The user with such credentials already exists.");
        }

        ApplicationUser user = to(userDto);

        user.setId(0);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = userDao.findRoleByName(APP_USER).orElseThrow(() -> {
            log.error("No user role found. Database is fucked up");
            return new ValidationException("No user role found. Please, contact support");
        });

        user.setRoles(singletonList(role));
        userDao.persist(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto findUserByUsername(String username) {
        log.debug("Fetching user from db with username {}", username);

        return from(userDao.findByUsername(username).orElseThrow(() -> {
            log.warn("Couldn't find user with username {}", username);
            return new ValidationException("No user with username %s found", username);
        }));
    }
}
