package org.sysc.ama.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class AnswerTest {

    private User author;
    private User amaSubject;
    private Ama  ama;
    private Question question;

    @Before
    public void setUp () {
        this.author     = new User();
        this.amaSubject = new User();
        this.ama        = new Ama("Foo", this.amaSubject, true);
        this.question   = new Question(this.author, this.ama, "How?");
    }

    @Test
    public void testCreateAnswer () {
        String body = "How is the weather?";

        // For notes on timing, see comment in PostTest.testCreatePost
        Date beforeCreated = new Date(new Date().getTime() - 5);
        Date afterCreated = new Date(new Date().getTime() + 5);

        Answer a = new Answer(this.amaSubject, this.ama, this.question, body);

        assertEquals(a.getAuthor(), this.amaSubject);
        assertEquals(a.getAma(), this.ama);
        assertEquals(a.getBody(), body);
        assertEquals(a.getQuestion(), this.question);
        assertFalse(a.isEdited());

        assertTrue(a.getCreated() instanceof Date);
        assertTrue(beforeCreated.before(a.getCreated()));
        assertTrue(afterCreated.after(a.getCreated()));
    }



}
