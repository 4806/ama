package org.sysc.ama.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PersistenceTest {

    @Autowired
    private AmaRepository amaRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testPersistAma() throws Exception {
        User user = new User("TestUser");
        Ama ama = new Ama("Test AMA", user, true);

        entityManager.persist(ama);
        entityManager.flush();

        Ama persistedAma = this.amaRepo.findById((long)1);

        assertEquals("Test AMA", persistedAma.getTitle());
        assertEquals( true, persistedAma.isPublic());
        assertEquals(user.getId(), ama.getSubject().getId());
    }
}
