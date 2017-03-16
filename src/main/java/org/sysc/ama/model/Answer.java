package org.sysc.ama.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.OneToOne;


@Entity
public class Answer extends Post {

    @OneToOne
    private Question question;

    public Answer () {}

    public Answer (User author, Ama ama, Question question, String body) {
        super(author, ama, body);
        this.question = question;
    }

    public void setQuestion (Question question) {
        this.question = question;
    }

    public Question getQuestion () {
        return this.question;
    }

}
