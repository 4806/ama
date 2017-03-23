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

        this.author.setId((long)1);
        this.amaSubject.setId((long)2);
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
    public void testUpVoteQuestion () {
        Question q = new Question(this.author, this.ama, "Who are you?");

        assertEquals(q.getUpVotes(), 0);
        q.upVote(this.amaSubject);
        assertEquals(q.getUpVotes(), 1);
    }

    @Test
    public void testDownVoteQuestion () {
        Question q = new Question(this.author, this.ama, "This is not a question");

        assertEquals(q.getDownVotes(), 0);
        q.downVote(this.amaSubject);
        assertEquals(q.getDownVotes(), 1);
    }

    @Test
    public void testHasVotedAfterUpVote () {
        Question q = new Question(this.author, this.ama, "What is your name?");

        q.upVote(this.amaSubject);
        assertTrue(q.hasVoted(this.amaSubject));
        assertFalse(q.hasVoted(this.author));
    }

    @Test
    public void testHasVotedAfterDownVote () {
        Question q = new Question(this.author, this.ama, "Bad question");

        q.downVote(this.amaSubject);
        assertTrue(q.hasVoted(this.amaSubject));
        System.out.println("MEWM: " + q.hasVoted(this.author));
        assertFalse(q.hasVoted(this.author));
    }


    @Test
    public void testRemoveUpVote () {
        Question q = new Question(this.author, this.ama, "Wazzz Up?");

        assertEquals(q.getUpVotes(), 0);

        q.upVote(this.amaSubject);
        assertEquals(q.getUpVotes(), 1);
        assertTrue(q.hasVoted(this.amaSubject));

        q.removeVote(this.amaSubject);
        assertEquals(q.getUpVotes(), 0);
        assertFalse(q.hasVoted(this.amaSubject));

    }

    @Test
    public void testRemoveDownVote () {
        Question q = new Question(this.author, this.ama, "Not a clue");

        assertEquals(q.getDownVotes(), 0);

        q.downVote(this.amaSubject);
        assertEquals(q.getDownVotes(), 1);
        assertTrue(q.hasVoted(this.amaSubject));

        q.removeVote(this.amaSubject);
        assertEquals(q.getDownVotes(), 0);
        assertFalse(q.hasVoted(this.amaSubject));

    }

    @Test
    public void testCanVoteAfterVoteRemoved () {
        Question q = new Question(this.author, this.ama, "Is it raining?");

        assertEquals(q.getUpVotes(), 0);
        assertEquals(q.getDownVotes(), 0);

        q.upVote(this.amaSubject);
        assertEquals(q.getUpVotes(), 1);
        assertEquals(q.getDownVotes(), 0);

        q.removeVote(this.amaSubject);
        q.downVote(this.amaSubject);
        assertEquals(q.getUpVotes(), 0);
        assertEquals(q.getDownVotes(), 1);

    }

}
