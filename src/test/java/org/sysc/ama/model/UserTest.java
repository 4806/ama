package org.sysc.ama.model;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

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

    @Test
    public void testFollowUserAddsUserToFollowing() throws Exception {
        User user = new User("BaseUser");
        User target = new User("Target");

        user.setId((long)1);
        target.setId((long)2);

        user.follow(target);
        assertTrue(user.getFollowing().contains(target));
    }

    @Test(expected = UserFollowException.class)
    public void testFollowAlreadyFollowedUser () throws Exception {
        User user = new User("TestUser");
        User target = new User("Target");

        user.setId((long)1);
        target.setId((long)2);

        user.follow(target);
        user.follow(target);
    }

    @Test(expected = UserFollowException.class)
    public void testCannotFollowSelf () throws Exception {
        User user = new User("TestUser");

        user.setId((long)1);
        user.follow(user);
    }

    @Test
    public void testJsonSerialization () throws Exception {
        User user = new User("TestUser");
        ObjectMapper mapper = new ObjectMapper();
        ReadContext ctx;

        user.setId((long)10);

        ctx = JsonPath.parse(mapper.writeValueAsString(user));

        assertEquals((int)ctx.read("$.id"), 10);
        assertEquals(ctx.read("$.name"), "TestUser");
        assertEquals(ctx.read("$.role"), "USER");
    }

    @Test(expected = com.jayway.jsonpath.PathNotFoundException.class)
    public void testJsonSerializationIgnoreFollowing () throws Exception {
        User user = new User("TestUser");
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(user);

        assertNull(JsonPath.parse(jsonString).read("$.following"));

    }

    @Test
    public void testUnfollowFollowedUser () throws Exception {
        User user = new User("TestUser");
        User target = new User("Target");

        user.setId((long)1);
        target.setId((long)2);

        user.follow(target);
        user.unfollow(target);
        assertEquals(user.getFollowing().size(), 0);
    }


    @Test(expected = UserUnfollowException.class)
    public void testUnfollowNonfollowedUser () throws Exception {
        User user = new User("TestUser");
        User target = new User("Target");

        user.setId((long)1);
        target.setId((long)2);

        // Do not already follow target user. Immediately unfollow and expect no error
        user.unfollow(target);
    }

}
