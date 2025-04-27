package myy803.social_book_store.model;


import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Collection;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

public class UserTest {

    @Test
    public void testGetAuthorities() {
        User user = new User();
        user.setRole(Role.USER);
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals(Role.USER.toString(), authorities.iterator().next().getAuthority());
    }

    @Test
    public void testGettersAndSetters() {
        User user = new User();
        user.setId(1);
        user.setUsername("john");
        user.setPassword("password");
        user.setRole(Role.USER);

        assertEquals(1, user.getId());
        assertEquals("john", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals(Role.USER, user.getRole());

    }

    @Test
    public void testIsAccountNonExpired() {
        User user = new User();
        assertEquals(true, user.isAccountNonExpired());
    }

    @Test
    public void testIsAccountNonLocked() {
        User user = new User();
        assertEquals(true, user.isAccountNonLocked());
    }

    @Test
    public void testIsCredentialsNonExpired() {
        User user = new User();
        assertEquals(true, user.isCredentialsNonExpired());
    }

    @Test
    public void testIsEnabled() {
        User user = new User();
        assertEquals(true, user.isEnabled());
    }
}
