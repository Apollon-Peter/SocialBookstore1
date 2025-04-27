package myy803.social_book_store.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import myy803.social_book_store.dao.ProfileDAO;
import myy803.social_book_store.dao.UserDAO;
import myy803.social_book_store.model.User;
import myy803.social_book_store.model.UserProfile;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private ProfileDAO profileDAO;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");

        when(userDAO.save(user)).thenReturn(user);
        
        userService.saveUser(user);
        
        verify(userDAO).save(user);
        verify(profileDAO).save(any(UserProfile.class));
    }

    @Test
    public void testIsUserPresent() {
        User user = new User();
        user.setUsername("testuser");

        when(userDAO.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        assertTrue(userService.isUserPresent(user));
        verify(userDAO).findByUsername(user.getUsername());
    }
    
    @Test
    public void testLoadUserByUsername() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");

        when(userDAO.findByUsername(username)).thenReturn(Optional.of(user));
        UserDetails userDetails = userService.loadUserByUsername(username);
        assertEquals(username, userDetails.getUsername());
    }

    @Test
    public void testLoadUserByUsernameUserNotFound() {
        String username = "testuser";

        when(userDAO.findByUsername(username)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));
    }

}
