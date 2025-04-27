package myy803.social_book_store.service;

import org.springframework.stereotype.Service;
import myy803.social_book_store.model.BookCategory;

@Service
public interface CategoryService {
	public void saveCategory(BookCategory category);
	public boolean isCategoryPresent(BookCategory category);
	public BookCategory findCategory(String category);
}
