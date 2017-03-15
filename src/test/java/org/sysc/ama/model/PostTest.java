package org.sysc.ama.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class PostTest {

    private User author;
    private User amaSubject;
    private Ama  ama;

    @Before
    public void setUp () {
        this.author     = new User();
        this.amaSubject = new User();
        this.ama        = new Ama("Foo", this.amaSubject, true);
    }

    @Test
    public void testCreatePost () {
        String body = "What is the meaning of life?";

        // Assumption
        // There is a window of 10 milliseconds for the new question to be created.
        // These dates are used to check that the question has been initialized with
        // the date of now. We cannot compare directly to now as there may be some
        // small delay between the created date and the testing date. This gives the
        // date comparison a small window.
        Date beforeCreated = new Date(new Date().getTime() - 5);
        Date afterCreated = new Date(new Date().getTime() + 5);

        Post p = new Post(this.author, this.ama, body);

        assertEquals(p.getAuthor(), this.author);
        assertEquals(p.getAma(), this.ama);
        assertEquals(p.getBody(), body);
        assertFalse(p.isEdited());

        assertTrue(p.getCreated() instanceof Date);
        assertTrue(beforeCreated.before(p.getCreated()));
        assertTrue(afterCreated.after(p.getCreated()));

    }

    @Test
    public void testEditingBody () {
        Post p = new Post(this.author, this.ama, "Why?");

        assertEquals(p.getBody(), "Why?");
        assertFalse(p.isEdited());

        p.setBody("How?");

        assertEquals(p.getBody(), "How?");
        assertTrue(p.isEdited());

    }
}
