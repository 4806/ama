package org.sysc.ama.model;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import java.util.List;
import java.util.ArrayList;

@Entity
public class Question extends Post {

    @ElementCollection
    private ArrayList<User> upVoters;

    @ElementCollection
    private ArrayList<User> downVoters;

    public Question () {}

    public Question (User user, Ama ama, String body) {
        super(user, ama, body);
        this.upVoters = new ArrayList<User>();
        this.downVoters = new ArrayList<User>();
    }

    public List<User> getUpVoters () {
        return this.upVoters;
    }

    public List<User> getDownVoters () {
        return this.downVoters;
    }

    public void upVote (User user) {
        if (!this.hasVoted(user)) {
            this.upVoters.add(user);
        }
    }

    public void downVote (User user) {
        if (!this.hasVoted(user)) {
            this.downVoters.add(user);
        }
    }

    public int getUpVotes () {
        return this.upVoters.size();
    }

    public int getDownVotes () {
        return this.downVoters.size();
    }

    public boolean hasVoted (User user) {
        return this.upVoters.contains(user) || this.downVoters.contains(user);
    }

    public void removeVote (User user) {
        if (this.upVoters.contains(user)) {
            this.upVoters.remove(user);
        }
        else if (this.downVoters.contains(user)) {
            this.downVoters.remove(user);
        }
    }

}
