package org.sysc.ama.model;

public class UserUnfollowException extends Exception {

    private User user;

    public UserUnfollowException (User user, String message) {
        super(message);
        this.user = user;
    }

    public User getUser () {
        return user;
    }


}
