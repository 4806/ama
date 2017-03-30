package org.sysc.ama.model;

import org.sysc.ama.controller.exception.BadRequestError;
import org.sysc.ama.model.User;

public class UserUnfollowException extends BadRequestError {

    private User user;

    public UserUnfollowException (User user, String message) {
        super(message);
        this.user = user;
    }

    public User getUser () {
        return user;
    }


}
