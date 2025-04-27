package myy803.social_book_store.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import myy803.social_book_store.model.Book;
import myy803.social_book_store.model.UserProfile;

@Service
public interface BookService {
	public void saveBook(Book book);
	public Book saveBookDetails(String title, String authors, String categories, String description);
	public Optional<Book> findByTitle(String title);
	public List<Book> findAll();
	public List<Book> findAllExceptCurrentUser(List<Book> bookOffers);
	public void deleteById(int bookId);
	public Optional<Book> findById(int bookId);
	public List<Book> findRequestedBooksByUser(UserProfile profile);
	
	public List<Book> findAllBasedOnProfileExceptCurrentUser(List<Book> bookOffers, UserProfile profile);
	public List<Book> findAllBasedOnProfile(UserProfile profile);
	public List<Book> exactSearch(UserProfile prof, String search);
	public List<Book> exactSearchExceptCurrentUser(List<Book> bookOffers, UserProfile prof, String search);
	public List<Book> approximateSearch(UserProfile prof, String search);
	public List<Book> approximateSearchExceptCurrentUser(List<Book> bookOffers, UserProfile prof, String search);
}
