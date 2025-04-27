package myy803.social_book_store.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import myy803.social_book_store.model.BookCategory;

public interface CategoryDAO extends JpaRepository<BookCategory, Integer> {
	Optional<BookCategory> findByName(String name);
	public BookCategory findById(int categoryId);

}
