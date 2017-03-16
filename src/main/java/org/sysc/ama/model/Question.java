package org.sysc.ama.model;

import javax.persistence.Entity;

@Entity
public class Question extends Post {

    public Question () {}

    public Question (User user, Ama ama, String body) {
        super(user, ama, body);
    }
}
