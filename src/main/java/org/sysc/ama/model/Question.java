package org.sysc.ama.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;

import javax.validation.constraints.Past;

import java.util.Date;

@Entity
public class Question {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User author;

    @Past
    private Date created;

    @NotEmpty
    private String body;

    private boolean edited;

    @ManyToOne
    private Ama ama;

    public Question () {}

    public Question (User author, Ama ama, String body) {
        this.author = author;
        this.ama    = ama;
        this.body   = body;
        this.created = new Date();
        this.edited = false;
    }

    public Long getId () {
        return this.id;
    }

    public boolean isEdited () {
        return this.edited;
    }

    public User getAuthor () {
        return this.author;
    }

    public Date getCreated () {
        return this.created;
    }

    public Ama getAma () {
        return this.ama;
    }

    public String getBody () {
        return this.body;
    }

    public void setBody (String body) {
        this.edited = true;
        this.body = body;
    }

}
