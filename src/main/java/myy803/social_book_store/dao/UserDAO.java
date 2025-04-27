package myy803.social_book_store.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import myy803.social_book_store.model.User;

public interface UserDAO extends JpaRepository<User, Integer> {
	Optional<User> findByUsername(String username);
	public User findById(int id);
}