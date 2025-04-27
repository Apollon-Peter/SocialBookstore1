package myy803.social_book_store.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import myy803.social_book_store.dao.BookDAO;
import myy803.social_book_store.model.Book;
import myy803.social_book_store.model.BookAuthor;
import myy803.social_book_store.model.BookCategory;

class BookServiceTest {
	
	@Mock
	private BookDAO bookDAO;
	
	@Mock
	private AuthorService authorService;
	
	@Mock 
	private CategoryService categoryService;
	
	@InjectMocks
	private BookServiceImpl bookService;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void testSaveBook() {
		
		Book book = new Book(1, "tesing", "test description", null, null, null);
		
		when(bookDAO.save(book)).thenReturn(book);
		when(bookDAO.findById(1)).thenReturn(Optional.of(book));
		
		bookService.saveBook(book);
		
		verify(bookDAO).save(book);
		assertTrue(bookService.findById(1).isPresent());
	}

	@Test
	void testSaveBookDetails() {
		
		BookAuthor author1 = new BookAuthor();
		author1.setName("tester1");
		BookAuthor author2 = new BookAuthor();
		author2.setName("tester2");
		
		BookCategory category = new BookCategory();
		category.setName("horror");
		
		when(authorService.isAuthorPresent(any(BookAuthor.class))).thenReturn(false);
		when(authorService.findAuthor("tester1")).thenReturn(author1);
		when(authorService.findAuthor("tester2")).thenReturn(author2);
		when(categoryService.isCategoryPresent(any(BookCategory.class))).thenReturn(false);
		when(categoryService.findCategory("horror")).thenReturn(category);
		
		Book savedBook = bookService.saveBookDetails("Test Book", "tester1,tester2", "horror", "This is a test description");
		
		assertEquals("Test Book", savedBook.getTitle());
		assertEquals("This is a test description", savedBook.getDescription());
		assertEquals(2, savedBook.getAuthors().size());
		assertEquals("horror", savedBook.getBookCategory().getName());
		
		verify(authorService, times(4)).saveAuthor(any(BookAuthor.class));
		verify(categoryService, times(1)).saveCategory(any(BookCategory.class));
		
	}

	@Test
	void testFindByTitle() {
		
		Book book = new Book();
		when(bookDAO.findByTitle("test")).thenReturn(Optional.of(book));
		
		Optional<Book> foundBook = bookService.findByTitle("test");
		
		assertTrue(foundBook.isPresent());
		assertEquals(book, foundBook.get());
		
	}

	@Test
	void testFindAll() {
		
		List<Book> books = new ArrayList<>();
		
		when(bookDAO.findAll()).thenReturn(books);
		List<Book> foundBooks = bookService.findAll();
		
		assertEquals(books, foundBooks);
		
	}

	@Test
	void testFindAllExceptCurrentUser() {
		
		List<Book> bookOffers = new ArrayList<>();
	    List<Book> expectedBooks = new ArrayList<>();
	    when(bookDAO.findAllExceptSpecificId(bookOffers)).thenReturn(expectedBooks);

	    List<Book> result = bookService.findAllExceptCurrentUser(bookOffers);

	    assertEquals(expectedBooks, result);
	    verify(bookDAO, times(1)).findAllExceptSpecificId(bookOffers);
	}

	@Test
	void testDeleteById() {
		
		Book book = new Book(1,"test", null, null, null, null);
		doNothing().when(bookDAO).deleteById(book.getBookId());
		
		bookService.deleteById(book.getBookId());
		verify(bookDAO, times(1)).deleteById(book.getBookId());
		
	}

	@Test
	void testFindById() {
		
		int bookId = 1;
        Book book = new Book();
        
        when(bookDAO.findById(bookId)).thenReturn(Optional.of(book));
        
        Optional<Book> foundBook = bookService.findById(bookId);
        
        assertTrue(foundBook.isPresent());
        assertEquals(book, foundBook.get());
	}

}
