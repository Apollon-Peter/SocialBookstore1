package myy803.social_book_store.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import myy803.social_book_store.model.UserProfile;


public interface ProfileDAO extends JpaRepository<UserProfile, Integer> {
	Optional<UserProfile> findByUsername(String username);
}
