package myy803.social_book_store.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import myy803.social_book_store.dao.BookDAO;
import myy803.social_book_store.dao.ProfileDAO;
import myy803.social_book_store.model.BookAuthor;
import myy803.social_book_store.model.BookCategory;
import myy803.social_book_store.model.UserProfile;

class ProfileServiceTest {
	
	@Mock
	private ProfileDAO profileDAO;
	
	@Mock
	private AuthorService authorService;
	
	@Mock
	private CategoryService categoryService;
	
	@InjectMocks
	private ProfileServiceImpl profileService;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void testSaveProfile() {
			
		UserProfile profile = new UserProfile(1, "tester", "Tester", "Perikleous 1", 21, "6945242094");
		
		when(profileDAO.save(profile)).thenReturn(profile);
		when(profileDAO.findById(1)).thenReturn(Optional.of(profile));
		
		profileService.saveProfile(profile);
		
		verify(profileDAO).save(profile);
		assertTrue(profileDAO.findById(1).isPresent());

	}


	@Test
	void testFindProfile() {
		
		UserProfile profile = new UserProfile(1, "tester", "Tester", "Perikleous 1", 21, "6945242094");
		
		when(profileDAO.findByUsername("tester")).thenReturn(Optional.of(profile));
		
		UserProfile foundProfile = profileService.findProfile("tester");
		
		verify(profileDAO).findByUsername("tester");
		assertEquals(profile, foundProfile);
	
	}

	@Test
	void testSaveProfileAuthors() {
		
		List<BookAuthor> authors = profileService.saveProfileAuthors("tester1, tester2");
		
		verify(authorService, times(2)).isAuthorPresent(any(BookAuthor.class));
		verify(authorService, times(2)).saveAuthor(any(BookAuthor.class));
		
		assertEquals(2, authors.size());
		
	}
	
	@Test
	void testSaveProfileCategories() {
		
		List<BookCategory> categories = profileService.saveProfileCategories("horror, scifi");
		
		verify(categoryService, times(2)).isCategoryPresent(any(BookCategory.class));
		verify(categoryService, times(2)).saveCategory(any(BookCategory.class));
		
		assertEquals(2, categories.size());
		
	}
	
	@Test
	void testFavAuthors() {
		
	    UserProfile profile = new UserProfile();
	    BookAuthor nikos = new BookAuthor(1,"NIKOS", null);
        BookAuthor apo = new BookAuthor(2, "APO", null);
        BookAuthor giorgos = new BookAuthor(3, "GIORGOS", null);

        List<BookAuthor> favoriteAuthors = Arrays.asList(nikos, apo, giorgos);

	    profile.setFavoriteBookAuthors(favoriteAuthors);
	    
	    String result = profileService.favAuthors(profile);

	    assertTrue(result.contains("NIKOS"));
	    assertTrue(result.contains("GIORGOS"));
	    assertTrue(result.contains("APO"));
	}
	
	@Test
	void testFavCategories() {
		
	    UserProfile profile = new UserProfile();
	    BookCategory category = new BookCategory(1, "horror", null);

        List<BookCategory> favoriteCategories = Arrays.asList(category);

	    profile.setFavoriteBookCategories(favoriteCategories);
	    
	    String result = profileService.favCategories(profile);

	    assertTrue(result.contains("horror"));

	}

}
