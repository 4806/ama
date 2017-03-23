package org.sysc.ama.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Question extends Post {

    @OneToOne(mappedBy="question")
    @JsonManagedReference
    private Answer answer;

    public Question () {}

    public Question (User user, Ama ama, String body) {
        super(user, ama, body);
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
}
