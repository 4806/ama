package org.sysc.ama.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.sysc.ama.model.Ama;
import org.sysc.ama.model.User;

public class AmaTest {

    @Test
    public void testGetAmaTitle() throws Exception {
        User user = new User("TestUser");
        Ama ama = new Ama("Test AMA", user, true);
        assertEquals("Test AMA", ama.getTitle());
    }

    @Test
    public void testAmaShouldBeLinkedToUser() throws Exception {
        User user = new User("TestUser");
        Ama ama = new Ama("Test AMA", user, true);
        assertEquals(user, ama.getSubject());
    }

    @Test
    public void testPublicAmaShouldBePublic() throws Exception {
        User user = new User("TestUser");
        Ama ama = new Ama("Test AMA", user, true);
        assertEquals(true, ama.isPublic());
    }

    @Test
    public void testPrivateAmaShouldBePrivate() throws Exception {
        User user = new User("TestUser");
        Ama ama = new Ama("Test AMA", user, false);
        assertEquals(false, ama.isPublic());
    }
}
