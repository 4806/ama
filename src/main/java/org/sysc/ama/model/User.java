package org.sysc.ama.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;

import javax.persistence.Entity;
import javax.validation.constraints.Pattern;

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

    public User() {
        this.role = Role.USER;
        //Default constructor for Spring
    }

    public User(String name) {
        this.name = name;
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

}
