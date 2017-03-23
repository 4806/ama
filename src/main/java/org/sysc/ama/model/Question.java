package org.sysc.ama.model;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import java.util.Map;
import java.util.HashMap;

@Entity
public class Question extends Post {

    private static Integer UP_VOTE = 1;
    private static Integer DOWN_VOTE = 0;

    @ElementCollection
    private Map<Long, Integer> voters = new HashMap<Long, Integer>();

    private int upVotes;
    private int downVotes;

    public Question () {}

    public Question (User user, Ama ama, String body) {
        super(user, ama, body);
        this.upVotes = 0;
        this.downVotes = 0;
    }

    public void setVoters (Map<Long, Integer> voters) {
        this.voters = voters;
    }

    public void upVote (User user) {
        if (!this.hasVoted(user)) {
            this.voters.put(user.getId(), Question.UP_VOTE);
            this.upVotes++;
        }
    }

    public void downVote (User user) {
        if (!this.hasVoted(user)) {
            this.voters.put(user.getId(), Question.DOWN_VOTE);
            this.downVotes++;
        }
    }

    public int getUpVotes () {
        return this.upVotes;
    }

    public int getDownVotes () {
        return this.downVotes;
    }

    public boolean hasVoted (User user) {
        return this.voters.containsKey(user.getId());
    }

    public void removeVote (User user) {
        Integer vote;

        if (this.hasVoted(user)) {
            vote = this.voters.get(user.getId());
            this.voters.remove(user.getId());

            if (vote == Question.UP_VOTE) {
                this.upVotes--;
            }
            else {
                this.downVotes--;
            }
        }
    }

}
