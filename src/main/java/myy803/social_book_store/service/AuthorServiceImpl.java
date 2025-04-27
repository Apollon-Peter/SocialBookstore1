package myy803.social_book_store.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import myy803.social_book_store.dao.AuthorDAO;
import myy803.social_book_store.model.BookAuthor;

@Service
public class AuthorServiceImpl implements AuthorService {
	@Autowired
	private AuthorDAO authorDAO;

	@Override
	public void saveAuthor(BookAuthor author) {
        authorDAO.save(author);	
	}

	@Override
	public boolean isAuthorPresent(BookAuthor author) {
		Optional<BookAuthor> storedAuthor = authorDAO.findByName(author.getName());
		return storedAuthor.isPresent();
	}

	@Override
	public BookAuthor findAuthor(String name) {
		return authorDAO.findByName(name).get();
	}

}
