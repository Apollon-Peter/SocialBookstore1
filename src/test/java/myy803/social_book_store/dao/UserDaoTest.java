package myy803.social_book_store.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import myy803.social_book_store.model.User;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserDaoTest {
	
	@Autowired
	private UserDAO userRepository;
	
	@Test
	void testFindByUsername() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("test");
        
        userRepository.save(user);
        
        User foundUser = userRepository.findByUsername("test").orElse(null);
        assertNotNull(foundUser);
        assertEquals("test", foundUser.getUsername());
    }

	@Test
	void testFindByIdInt() {
	    User user = new User();
	    user.setUsername("test");
	    user.setPassword("test");
	    
	    userRepository.save(user);
	    
	    User foundUser = userRepository.findById(user.getId());
	    
	    assertNotNull(foundUser);
	    assertEquals(user.getId(), foundUser.getId());
	}

}
