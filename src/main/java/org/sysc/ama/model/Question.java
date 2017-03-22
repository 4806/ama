package org.sysc.ama.model;

import javax.persistence.Entity;

@Entity
public class Question extends Post {

    private int upvotes;
    private int downvotes;

    public Question () {}

    public Question (User user, Ama ama, String body) {
        super(user, ama, body);
        this.upvotes = 0;
        this.downvotes = 0;
    }

    public void upvote () {
        this.upvotes++;
    }

    public void downvote () {
        this.downvotes++;
    }

    public int getUpvotes () {
        return this.upvotes;
    }

    public int getDownvotes () {
        return this.downvotes;
    }


}
