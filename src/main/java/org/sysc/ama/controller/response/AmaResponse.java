package org.sysc.ama.controller.response;

import org.sysc.ama.model.Ama;
import org.sysc.ama.model.User;

public class AmaResponse extends Ama {

    public AmaResponse (Ama ama, User activeUser) {
        super(ama);
        this.setSubject(new UserResponse(ama.getSubject(), activeUser));
    }
}
