package org.sysc.ama.controller.response;

import org.sysc.ama.model.User;

public class UserResponse extends User {

    private boolean followed;

    public UserResponse (User user, User activeUser) {
        super(user);
        this.followed = activeUser.isFollowing(user);
    }


    public void setFollowed (boolean followed) {
        this.followed = followed;
    }

    public boolean getFollowed () {
        return this.followed;
    }

}
