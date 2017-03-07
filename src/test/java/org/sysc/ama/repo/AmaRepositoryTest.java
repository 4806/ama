package org.sysc.ama.repo;

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

import org.sysc.ama.model.User;
import org.sysc.ama.model.Ama;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AmaRepositoryTest {

    @Autowired
    private AmaRepository amaRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testPersistAma() throws Exception {
        User user = new User("TestUser");
        Ama ama = new Ama("Test AMA", user, true);

        entityManager.persist(user);
        entityManager.persist(ama);
        entityManager.flush();

        Ama persistedAma = this.amaRepo.findById((long)1).get();

        assertEquals("Test AMA", persistedAma.getTitle());
        assertEquals( true, persistedAma.isPublic());
        assertEquals(user.getId(), ama.getSubject().getId());
    }
}
