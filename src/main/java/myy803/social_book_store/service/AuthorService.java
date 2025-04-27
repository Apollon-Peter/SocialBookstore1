package myy803.social_book_store.service;

import org.springframework.stereotype.Service;

import myy803.social_book_store.model.BookAuthor;

@Service
public interface AuthorService {
	public void saveAuthor(BookAuthor author);
	public boolean isAuthorPresent(BookAuthor author);
	public BookAuthor findAuthor(String name);
}
