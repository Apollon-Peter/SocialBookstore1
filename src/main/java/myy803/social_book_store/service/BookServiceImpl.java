package myy803.social_book_store.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import myy803.social_book_store.dao.BookDAO;
import myy803.social_book_store.model.Book;
import myy803.social_book_store.model.BookAuthor;
import myy803.social_book_store.model.BookCategory;
import myy803.social_book_store.model.UserProfile;

@Service
public class BookServiceImpl implements BookService {
	
	@Autowired
	private BookDAO bookDAO;
	@Autowired
	AuthorService authorService;
	@Autowired
	CategoryService categoryService;

	@Override
	public void saveBook(Book book) {
		bookDAO.save(book);
	}
	
	@Override 
	public Book saveBookDetails(String title, String authors, String categories, String description) {
		List<String> Authors = Arrays.asList(authors.split("\\s*,\\s*"));
		List<BookAuthor> BookAuthors = new ArrayList();
		BookCategory category = new BookCategory();
		Book book = new Book();
		
		for (String auth: Authors) {
			BookAuthor bookAuthor = new BookAuthor();
			bookAuthor.setName(auth);
			
			if (!authorService.isAuthorPresent(bookAuthor)) {
				authorService.saveAuthor(bookAuthor);
			}
			
			bookAuthor = authorService.findAuthor(auth);
			
			List<Book> books = bookAuthor.getBooks();
			books.add(book);
			bookAuthor.setBooks(books);

			BookAuthors.add(bookAuthor);

			authorService.saveAuthor(bookAuthor);

		}
		
		category.setName(categories);

		if (!categoryService.isCategoryPresent(category)) {
			categoryService.saveCategory(category);
		}
		
		category = categoryService.findCategory(categories);
		
		book.setTitle(title);
		book.setDescription(description);
		book.setAuthors(BookAuthors);
		book.setBookCategory(category);

		return book;
		
	}

	@Override
	public Optional<Book> findByTitle(String title) {
		Optional<Book> book = bookDAO.findByTitle(title);
		if (book != null) {
			return book;
		} else {
			throw new RuntimeException("Didnot find book id - " + title);
		}
	}
	
	@Override
	public List<Book> findAll() {
		return bookDAO.findAll();
	}

	@Override
	//Find all the books except the ones that the logged in user posted
	public List<Book> findAllExceptCurrentUser(List<Book> bookOffers) {
		return bookDAO.findAllExceptSpecificId(bookOffers);
	}

	@Override
	@Transactional
	public void deleteById(int bookId) {
		bookDAO.deleteById(bookId);
	}

	@Override
	public Optional<Book> findById(int bookId) {
		Optional<Book> book = bookDAO.findById(bookId);
		if (book != null) {
			return book;
		} else {
			throw new RuntimeException("Didnot find book id - " + bookId);
		}
	}

	@Override
	public List<Book> findRequestedBooksByUser(UserProfile profile) {
		return bookDAO.findRequestedBooksByUserId(profile.getId());		
	}

	@Override
	public List<Book> findAllBasedOnProfileExceptCurrentUser(List<Book> bookOffers, UserProfile profile) {
		return bookDAO.findAllBasedOnProfileExceptCurrentUser(bookOffers, profile.getId());
	}

	@Override
	public List<Book> findAllBasedOnProfile(UserProfile profile) {
		return bookDAO.findAllBasedOnProfile(profile.getId());
	}

	@Override
	public List<Book> exactSearch(UserProfile profile, String search) {
		return bookDAO.findAllBasedOnSearch(profile.getId(), search);
	}

	@Override
	public List<Book> exactSearchExceptCurrentUser(List<Book> bookOffers, UserProfile profile, String search) {
		return bookDAO.findAllBasedOnSearchExceptCurrentUser(bookOffers, profile.getId(), search);
	}

	@Override
	public List<Book> approximateSearch(UserProfile profile, String search) {
		return bookDAO.approximateSearch(profile.getId(), search);
	}

	@Override
	public List<Book> approximateSearchExceptCurrentUser(List<Book> bookOffers, UserProfile profile, String search) {
		return bookDAO.approximateSearchExceptCurrentUser(bookOffers, profile.getId(), search);
	}
	
}
