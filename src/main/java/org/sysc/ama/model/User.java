package org.sysc.ama.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;

import javax.validation.constraints.Pattern;

@Entity
public class User {

    @NotEmpty
    @Column(unique = true)
    @Pattern(regexp="^[a-zA-Z][a-zA-Z0-9_]*$")
    private String name;

    @Id
    @GeneratedValue
    private Long id;

    public User() {
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
}
