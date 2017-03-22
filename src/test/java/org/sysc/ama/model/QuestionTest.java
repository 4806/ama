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
        String body = "How is the weather?";

        // For notes on timing, see comment in PostTest.testCreatePost
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
    public void testUpvoteQuestion () {
        Question q = new Question(this.author, this.ama, "Who are you?");

        assertEquals(q.getUpVotes(), 0);
        q.upVote(this.amaSubject);
        assertEquals(q.getUpVotes(), 1);
    }

    @Test
    public void testDownvoteQuestion () {
        Question q = new Question(this.author, this.ama, "This is not a question");

        assertEquals(q.getDownVotes(), 0);
        q.downVote(this.amaSubject);
        assertEquals(q.getDownVotes(), 1);
    }

    @Test
    public void testHasVotedAfterUpvote () {
        Question q = new Question(this.author, this.ama, "What is your name?");

        q.upVote(this.amaSubject);
        assertTrue(q.hasVoted(this.amaSubject));
        assertFalse(q.hasVoted(this.author));
    }

    @Test
    public void testHasVotedAfterDownvote () {
        Question q = new Question(this.author, this.ama, "Bad question");

        q.downVote(this.amaSubject);
        assertTrue(q.hasVoted(this.amaSubject));
        assertFalse(q.hasVoted(this.author));
    }


}
