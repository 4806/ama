package org.sysc.ama.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class QuestionTest {

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
    public void testCreateQuestion () {
        String body = "What is the meaning of life?";

        // Assumption
        // There is a window of 10 milliseconds for the new question to be created.
        // These dates are used to check that the question has been initialized with
        // the date of now. We cannot compare directly to now as there may be some
        // small delay between the created date and the testing date. This gives the
        // date comparison a small window.
        Date beforeCreated = new Date(new Date().getTime() - 5);
        Date afterCreated = new Date(new Date().getTime() + 5);

        Question q = new Question(this.author, this.ama, body);

        assertEquals(q.getAuthor(), this.author);
        assertEquals(q.getAma(), this.ama);
        assertEquals(q.getBody(), body);
        assertFalse(q.isEdited());

        assertTrue(q.getCreated() instanceof Date);
        assertTrue(beforeCreated.before(q.getCreated()));
        assertTrue(afterCreated.after(q.getCreated()));

    }

    @Test
    public void testEditingBody () {
        Question q = new Question(this.author, this.ama, "Why?");

        assertEquals(q.getBody(), "Why?");
        assertFalse(q.isEdited());

        q.setBody("How?");

        assertEquals(q.getBody(), "How?");
        assertTrue(q.isEdited());

    }
}
