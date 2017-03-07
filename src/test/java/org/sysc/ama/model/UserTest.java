package org.sysc.ama.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.Validator;

import static org.junit.Assert.assertEquals;

import org.sysc.ama.repo.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserTest {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TestEntityManager entityManager;

    // @Test
    public void testPersistUser() throws Exception {
        User user = new User("TestUser");

        entityManager.persist(user);
        entityManager.flush();

        User persistedUser = this.userRepo.findById(user.getId()).get();


        assertEquals("TestUser", persistedUser.getName());
    }

    @Test(expected = javax.persistence.PersistenceException.class)
    public void testUserNameMustBeUnique() throws Exception {
        User user = new User("TestUser");
        User user2 = new User("TestUser");
        entityManager.persist(user);
        entityManager.persist(user2);
        entityManager.flush();
    }
}
