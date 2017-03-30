package org.sysc.ama.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.FetchType;
import javax.validation.constraints.Pattern;

import java.util.List;
import java.util.ArrayList;

@Entity
public class User {

    public enum Role {
        USER, ADMIN
    }

    @NotEmpty
    @Column(unique = true)
    @Pattern(regexp="^[a-zA-Z][a-zA-Z0-9_]*$")
    private String name;

    @JsonIgnore
    private String passwordHash;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private List<User> following = new ArrayList<User>();

    public User() {

        this.role = Role.USER;
        this.passwordHash = "";

    }

    public User(String name) {
        this();
        this.name = name;
    }

    public User (User user) {
        this.id = user.id;
        this.role = user.role;
        this.following = user.following;
        this.passwordHash = user.passwordHash;
    }


    public Long getId(){
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setPassword(String unhashedPassword) {
        this.passwordHash = new BCryptPasswordEncoder().encode(unhashedPassword);
    }

    public List<User> getFollowing () {
        return this.following;
    }

    public boolean isFollowing (User user) {
        for (User u : this.following) {
            if (u.getId() == user.getId()) {
                return true;
            }
        }
        return false;
    }

    public void follow (User user) throws UserFollowException {
        if (user.getId() == this.getId()) {
            throw new UserFollowException(user, "Cannot follow self");
        }

        for (User u : this.following) {
            if (u.getId() == user.getId()) {
                throw new UserFollowException(user, "Cannot follow already followed user");
            }
        }
        this.following.add(user);
    }

    public void unfollow (User user) throws UserUnfollowException {
        User u;
        for (int i = 0; i < this.following.size(); i++) {
            u = this.following.get(i);
            if (u.getId() == user.getId()) {
                this.following.remove(user);
                return;
            }
        }
        throw new UserUnfollowException(user, "User is not being followed");
    }
}
