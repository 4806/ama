package org.sysc.ama.model;

import javax.persistence.ElementCollection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import java.util.Map;
import java.util.HashMap;

@Entity
public class Question extends Post {

    public enum VoteType {
        UP_VOTE,
        DOWN_VOTE,
        NO_VOTE
    }

    @ElementCollection
    @JsonIgnore
    private Map<Long, VoteType> voters = new HashMap<>();

    private int upVotes;
    private int downVotes;

    @OneToOne(mappedBy="question")
    @JsonManagedReference
    private Answer answer;


    public Question () {}

    public Question (Question other) {
        this(other.getAuthor(), other.getAma(), other.getBody());
        this.upVotes = other.upVotes;
        this.downVotes = other.downVotes;
        this.voters = other.voters;
        this.answer = other.answer;
        this.id = other.id;
    }

    public Question (User user, Ama ama, String body) {
        super(user, ama, body);
        this.upVotes = 0;
        this.downVotes = 0;
    }

    public void setVoters (Map<Long, VoteType> voters) {
        this.voters = voters;
    }


    public void upVote (User user) {
        if (!this.hasVoted(user)) {
            this.voters.put(user.getId(), VoteType.UP_VOTE);
            this.upVotes++;
        }
    }

    public void downVote (User user) {
        if (!this.hasVoted(user)) {
            this.voters.put(user.getId(), VoteType.DOWN_VOTE);
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

    public VoteType getUserVote (User user) {
        if (!hasVoted(user)){
            return VoteType.NO_VOTE;
        }
        return this.voters.get(user.getId());
    }


    public void removeVote (User user) {
        VoteType vote;

        if (this.hasVoted(user)) {
            vote = this.voters.get(user.getId());
            this.voters.remove(user.getId());

            if (vote == VoteType.UP_VOTE) {
                this.upVotes--;
            }
            else {
                this.downVotes--;
            }
        }
    }


    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

}
