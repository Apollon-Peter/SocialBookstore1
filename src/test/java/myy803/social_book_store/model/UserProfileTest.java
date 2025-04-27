package myy803.social_book_store.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserProfileTest {

    private UserProfile profile;

    @BeforeEach
    public void setup() {
        profile = new UserProfile(1, "tester", "Tester", "Perikleous 1", 21, "6945242094");;
    }

    @Test
    public void testGetters() {
    	
        assertEquals("tester", profile.getUsername());
        assertEquals("Tester", profile.getFullname());
        assertEquals("Perikleous 1", profile.getAddress());
        assertEquals(21, profile.getAge());
        assertEquals("6945242094", profile.getPhone());
    }

    @Test
    public void testInitializationWithIdAndUsername() {
    	
        UserProfile profile = new UserProfile(1, "tester2");
        
        assertEquals(1, profile.getId());
        assertEquals("tester2", profile.getUsername());
        assertNull(profile.getFullname());
        assertEquals(0, profile.getAge());
        assertNull(profile.getAddress());
        assertNull(profile.getPhone());
        assertNull(profile.getFavoriteBookAuthors());
        assertNull(profile.getFavoriteBookCategories());
        assertNull(profile.getBookOffers());
    }

    @Test
    public void testNotifications() {
    	
        List<String> notifications = new ArrayList<>();
        notifications.add("Notification 1");
        notifications.add("Notification 2");

        profile.setNotifications(notifications);
        assertEquals(notifications, profile.getNotifications());
    }

}
