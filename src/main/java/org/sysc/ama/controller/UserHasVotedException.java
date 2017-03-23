package org.sysc.ama.controller;

import org.sysc.ama.model.User;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UserHasVotedException extends RuntimeException {

    private User user;

    public UserHasVotedException (User user){
        super();
        this.user = user;
    }

    public User getUser () {
        return user;
    }
}
