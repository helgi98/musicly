package edu.lnu.musicly.streaming.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NamedQueries({
        @NamedQuery(name = ApplicationUser.FIND_BY_USERNAME, query = "SELECT au FROM ApplicationUser au WHERE au.username = :username"),
        @NamedQuery(name = ApplicationUser.CHECK_IF_UNIQUE, query = "SELECT (CASE WHEN COUNT(au) > 0 THEN FALSE ELSE TRUE END) " +
                "FROM ApplicationUser au WHERE au.username = :username OR au.email = :email")
})
@EqualsAndHashCode(exclude = {"roles"})
@ToString(exclude = {"roles"})
public class ApplicationUser {
    public static final String FIND_BY_USERNAME = "findByUsername";
    public static final String CHECK_IF_UNIQUE = "checkIfUnique";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq")
    private long id;

    private String username;

    private String email;

    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USER_ROLE", joinColumns = {
            @JoinColumn(name = "USER_ID")
    }, inverseJoinColumns = {
            @JoinColumn(name = "ROLE_ID")
    })
    private List<Role> roles;
}
