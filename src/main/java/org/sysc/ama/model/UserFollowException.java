package org.sysc.ama.model;

public class UserFollowException extends Exception {

    private User user;

    public UserFollowException (User user, String message) {
        super(message);
        this.user = user;
    }

    public User getUser () {
        return user;
    }


}
