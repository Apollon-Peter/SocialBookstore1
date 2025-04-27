package myy803.social_book_store.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import myy803.social_book_store.dao.CategoryDAO;
import myy803.social_book_store.dao.ProfileDAO;
import myy803.social_book_store.model.BookCategory;

class CategoryServiceTest {
	
	@Mock
	private CategoryDAO categoryDAO;
	
	@InjectMocks
	private CategoryServiceImpl categoryService;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testSaveCategory() {
		
	    BookCategory category = new BookCategory();
	    categoryService.saveCategory(category);

	    verify(categoryDAO, times(1)).save(category);
	}

	@Test
	void testIsCategoryPresent() {

	    BookCategory category = new BookCategory();
	    category.setName("horror");

	    when(categoryDAO.findByName("horror")).thenReturn(Optional.of(category));

	    assertTrue(categoryService.isCategoryPresent(category));
	    verify(categoryDAO, times(1)).findByName("horror");
	}

	@Test
	void testFindCategory() {
	   
	    BookCategory category = new BookCategory();
	    category.setName("horror");

	    when(categoryDAO.findByName("horror")).thenReturn(Optional.of(category));

	    BookCategory foundCategory = categoryService.findCategory("horror");
	    
	    verify(categoryDAO, times(1)).findByName("horror");
	    assertEquals(category, foundCategory);
	}

}
