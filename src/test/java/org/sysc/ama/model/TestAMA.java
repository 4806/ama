package org.sysc.ama.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.sysc.ama.model.Ama;
import org.sysc.ama.model.User;

/**
 * Created by Cameron on 27/02/2017.
 */
public class TestAMA {
    @Test
    public void amaShouldBeLinkedToUser() throws Exception {
        User user = new User();
        Ama ama = new Ama("Test AMA", user, true);
        assertEquals(ama.getSubject(), user);
    }

    @Test
    public void publicAmaShouldBePublic() throws Exception {
        User user = new User();
        Ama ama = new Ama("Test AMA", user, true);
        assertEquals(ama.getSubject(), user);
    }

    @Test
    public void privateAmaShouldBePrivate() throws Exception {
        User user = new User();
        Ama ama = new Ama("Test AMA", user, false);
        assertEquals(ama.getSubject(), user);
    }
}
