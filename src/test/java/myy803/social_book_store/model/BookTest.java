package myy803.social_book_store.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class BookTest {
	
	private Book book;

	@Test
	public void testGetters() {
		BookAuthor nikos = new BookAuthor();
		BookAuthor apo = new BookAuthor();
		BookAuthor giorgos = new BookAuthor();
		List<BookAuthor> authors = new ArrayList<>(Arrays.asList(nikos,apo,giorgos));
		
		BookCategory category = new BookCategory(1,"horror", null);
		
		UserProfile tester = new UserProfile(1,"john_doe", "John Doe", "123 Main St", 30, "123-456-7890");
		List<UserProfile> profs = new ArrayList<>(Arrays.asList(tester));
		book = new Book(1, "title", "description", authors, category, profs);
		
		assertEquals(1, book.getBookId());
		assertEquals("title", book.getTitle());
		assertEquals("description", book.getDescription());
		assertEquals(authors, book.getAuthors());
		assertEquals(category, book.getBookCategory());
		assertEquals(profs, book.getRequestingUsers());
	}

}
