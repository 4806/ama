package org.sysc.ama.controller.exception;

import org.sysc.ama.model.User;

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
