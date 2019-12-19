package edu.lnu.musicly.streaming.dao;

import edu.lnu.musicly.streaming.entities.ApplicationUser;
import edu.lnu.musicly.streaming.entities.Role;

import java.util.Optional;

public interface ApplicationUserDao extends GenericDao<ApplicationUser, Long> {
    Optional<ApplicationUser> findByUsername(String username);

    boolean isUnique(String username, String email);

    Optional<Role> findRoleByName(String roleName);
}
