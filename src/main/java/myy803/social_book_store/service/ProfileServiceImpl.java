package myy803.social_book_store.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import myy803.social_book_store.dao.ProfileDAO;
import myy803.social_book_store.model.BookAuthor;
import myy803.social_book_store.model.BookCategory;
import myy803.social_book_store.model.UserProfile;

@Service
public class ProfileServiceImpl implements ProfileService {
	
	@Autowired
	private ProfileDAO profileDAO;
	
	@Autowired
	private AuthorService authorService;
	
	@Autowired
	private CategoryService categoryService;

	@Override
	public void saveProfile(UserProfile prof) {
		profileDAO.save(prof);
	}
	
	@Override
	public void update(UserProfile prof) {
		profileDAO.save(prof);
	}
	
	@Override
	public UserProfile findProfile(String username) {
		return profileDAO.findByUsername(username).get();
	}

	@Override
	public List<BookAuthor> saveProfileAuthors(String authors) {
		List<String> authorStrings = Arrays.asList(authors.split("\\s*,\\s*"));
		List<BookAuthor> authorList = new ArrayList();
		for (String auth: authorStrings) {
			BookAuthor bookAuthor = new BookAuthor();
			bookAuthor.setName(auth);
			
			if (!authorService.isAuthorPresent(bookAuthor)) {
				authorService.saveAuthor(bookAuthor);
			}
			
			bookAuthor = authorService.findAuthor(auth);
			
			authorList.add(bookAuthor);

			authorService.saveAuthor(bookAuthor);

		}
		return authorList;
	}

	@Override
	public List<BookCategory> saveProfileCategories(String categories) {
		List<String> categoryStrings = Arrays.asList(categories.split("\\s*,\\s*"));
		List<BookCategory> categoryList = new ArrayList();
		for (String categ: categoryStrings) {
			BookCategory bookCategory = new BookCategory();
			bookCategory.setName(categ);
			
			if (!categoryService.isCategoryPresent(bookCategory)) {
				categoryService.saveCategory(bookCategory);
			}
			
			bookCategory = categoryService.findCategory(categ);
			
			categoryList.add(bookCategory);

			categoryService.saveCategory(bookCategory);

		}
		return categoryList;
	}

	@Override
	public String favAuthors(UserProfile prof) {
		List<BookAuthor> favAuthors = prof.getFavoriteBookAuthors();
        String favoriteAuthors = "";
        for (BookAuthor auth : favAuthors) {
        	favoriteAuthors += (auth.getName() + ", ");
        }
        return favoriteAuthors;
	}

	@Override
	public String favCategories(UserProfile prof) {
        List<BookCategory> favCategories = prof.getFavoriteBookCategories();
        String favoriteCategories = "";
        for (BookCategory categ: favCategories) {
        	favoriteCategories += (categ.getName() + ", ");
        }
		return favoriteCategories;
	}
}
