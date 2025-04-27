package myy803.social_book_store.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import myy803.social_book_store.dao.CategoryDAO;
import myy803.social_book_store.model.BookCategory;

@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryDAO categoryDAO;
	
	@Override
	public void saveCategory(BookCategory category) {
		categoryDAO.save(category);
	}

	@Override
	public boolean isCategoryPresent(BookCategory category) {
		Optional<BookCategory> categ = categoryDAO.findByName(category.getName());
		return categ.isPresent();
	}

	@Override
	public BookCategory findCategory(String category) {
		return categoryDAO.findByName(category).get();
	}

}
