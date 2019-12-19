package edu.lnu.musicly.streaming.dto;

import edu.lnu.musicly.streaming.entities.ApplicationUser;
import edu.lnu.musicly.streaming.entities.Role;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

import static edu.lnu.musicly.streaming.utils.CollectionUtils.mapToList;

@Data
public class UserDto {

    private long id;

    @Size(min = 6, max = 64)
    private String username;

    @Email
    private String email;

    @NotBlank
    private String password;

    private List<String> roles;

    public static UserDto from(ApplicationUser user) {
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());
        userDto.setRoles(mapToList(user.getRoles(), Role::getRole));

        return userDto;
    }

    public static ApplicationUser to(UserDto userDto) {
        ApplicationUser user = new ApplicationUser();

        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setUsername(userDto.getUsername());

        return user;
    }
}
