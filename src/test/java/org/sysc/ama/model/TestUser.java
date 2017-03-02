package org.sysc.ama.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.Validator;

import static org.junit.Assert.assertEquals;

/**
 * Created by cameronblanchard on 3/2/2017.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class TestUser {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testPersistUser() throws Exception {
        User user = new User("TestUser");

        entityManager.persist(user);
        entityManager.flush();

        User persistedUser = this.userRepo.findById((long)1);

        assertEquals("TestUser", persistedUser.getName());
    }

    @Test(expected = javax.persistence.PersistenceException.class)
    public void userNameMustBeUnique() throws Exception {
        User user = new User("TestUser");
        User user2 = new User("TestUser");
        entityManager.persist(user);
        entityManager.persist(user2);
        entityManager.flush();
    }
}
