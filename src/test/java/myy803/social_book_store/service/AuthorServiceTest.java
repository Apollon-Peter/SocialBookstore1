package myy803.social_book_store.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import myy803.social_book_store.dao.AuthorDAO;
import myy803.social_book_store.dao.AuthorDAO;
import myy803.social_book_store.model.BookAuthor;

class AuthorServiceTest {

	@Mock
	private AuthorDAO authorDAO;
	
	@InjectMocks
	private AuthorServiceImpl authorService;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testSaveAuthor() {
		
	    BookAuthor author = new BookAuthor();
	    authorService.saveAuthor(author);

	    verify(authorDAO, times(1)).save(author);
	}

	@Test
	void testIsAuthorPresent() {

	    BookAuthor author = new BookAuthor();
	    author.setName("tester");

	    when(authorDAO.findByName("tester")).thenReturn(Optional.of(author));

	    assertTrue(authorService.isAuthorPresent(author));
	    verify(authorDAO, times(1)).findByName("tester");
	}

	@Test
	void testFindAuthor() {
	   
	    BookAuthor Author = new BookAuthor();
	    Author.setName("tester");

	    when(authorDAO.findByName("tester")).thenReturn(Optional.of(Author));

	    BookAuthor foundAuthor = authorService.findAuthor("tester");
	    
	    verify(authorDAO, times(1)).findByName("tester");
	    assertEquals(Author, foundAuthor);
	}

}
