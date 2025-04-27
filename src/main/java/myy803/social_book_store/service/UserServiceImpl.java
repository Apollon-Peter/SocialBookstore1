package myy803.social_book_store.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import myy803.social_book_store.dao.ProfileDAO;
import myy803.social_book_store.dao.UserDAO;
import myy803.social_book_store.model.User;
import myy803.social_book_store.model.UserProfile;

@Service
public class UserServiceImpl implements UserService, UserDetailsService{

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private ProfileDAO profileDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@Override
	public void saveUser(User user) {
		String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userDAO.save(user);
        UserProfile profile = new UserProfile(user.getId(), user.getUsername());
        profile.setUser(user);
        profileDAO.save(profile);
    }

	@Override
	public boolean isUserPresent(User user) {
		Optional<User> storedUser = userDAO.findByUsername(user.getUsername());
		return storedUser.isPresent();
	}
	
	@Override
	public Optional<User> findById(String username) {
		Optional<User> user = userDAO.findByUsername(username);
		
		if(user != null) {
			
			return user;
		}
		else {
			throw new RuntimeException("Did not find user id - " + username);
		}	
	}

	// Method defined in Spring Security UserDetailsService interface
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// orElseThrow method of Optional container that throws an exception if Optional result  is null
		return userDAO.findByUsername(username).orElseThrow(
	                ()-> new UsernameNotFoundException(
	                        String.format("USER_NOT_FOUND %s", username)
	                ));
	}

	@Override
	@Transactional
	public void deleteUser(User user) {
		userDAO.deleteById(user.getId());
		userDAO.delete(user);
	}
}
