package org.sysc.ama.model;

import org.sysc.ama.controller.exception.BadRequestError;
import org.sysc.ama.model.User;

public class UserFollowException extends BadRequestError {

    private User user;

    public UserFollowException (User user, String message) {
        super(message);
        this.user = user;
    }

    public User getUser () {
        return user;
    }


}
