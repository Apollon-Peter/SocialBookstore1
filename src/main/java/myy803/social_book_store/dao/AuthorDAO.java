package myy803.social_book_store.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import myy803.social_book_store.model.BookAuthor;

public interface AuthorDAO extends JpaRepository<BookAuthor, Integer> {
	Optional<BookAuthor> findByName(String name);
}
