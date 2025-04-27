package myy803.social_book_store.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import myy803.social_book_store.model.User;

@Service
public interface UserService {
	public void saveUser(User user);
    public boolean isUserPresent(User user);
    public Optional<User> findById(String username);
    public void deleteUser(User user);
}

