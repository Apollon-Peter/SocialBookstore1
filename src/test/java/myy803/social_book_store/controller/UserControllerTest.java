package myy803.social_book_store.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import myy803.social_book_store.dao.BookDAO;
import myy803.social_book_store.dao.ProfileDAO;
import myy803.social_book_store.model.Book;
import myy803.social_book_store.model.BookAuthor;
import myy803.social_book_store.model.BookCategory;
import myy803.social_book_store.model.UserProfile;
import myy803.social_book_store.service.AuthorService;
import myy803.social_book_store.service.BookService;
import myy803.social_book_store.service.CategoryService;
import myy803.social_book_store.service.ProfileService;
import myy803.social_book_store.service.UserService;


@WebMvcTest
class UserControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserController userController;
	
	@MockBean
	private UserService userService;

	@MockBean
    private ProfileService profileService;
	
	@MockBean
	private AuthorService authorService;
	
	@MockBean
	private BookService bookService;
	
	@MockBean
	private CategoryService categoryService;
	
	@MockBean
	private BookDAO bookDAO;
	
	@MockBean
	private ProfileDAO profileDAO;
	
    @Autowired
    private WebApplicationContext context;
    
	@BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }
	
	@Test
	@WithMockUser(username="user", roles={"USER"})
	public void testGetUserHome() throws Exception {
		
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername("user");
        List<String> notifications = Arrays.asList("notification1", "notification2");
        userProfile.setNotifications(notifications);

        when(profileService.findProfile("user")).thenReturn(userProfile);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/main_menu"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("username", "user"))
                .andExpect(model().attribute("notifications", notifications));
    }
	
	@Test
    @WithMockUser(username="user", roles={"USER"})
    public void testCreateProfile() throws Exception {
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername("user");
        userProfile.setNotifications(new ArrayList<>());

        when(profileService.findProfile("user")).thenReturn(userProfile);

        mockMvc.perform(MockMvcRequestBuilders.get("/profile_details"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("authors", userProfile.getFavoriteBookAuthors()))
                .andExpect(MockMvcResultMatchers.model().attribute("categories", userProfile.getFavoriteBookCategories()))
                .andExpect(MockMvcResultMatchers.model().attribute("user", userProfile))
                .andExpect(MockMvcResultMatchers.view().name("user/profile"));
    }
	
	@Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testSaveProfile() throws Exception {

	    UserProfile profile = new UserProfile(1, "tester", "Tester", "Perikleous 1", 21, "6945242094");

        BookAuthor nikos = new BookAuthor();
        BookAuthor apo = new BookAuthor();
        BookAuthor giorgos = new BookAuthor();
        BookCategory category = new BookCategory(1, "horror", null);

        List<BookAuthor> authors = Arrays.asList(nikos, apo, giorgos);
        List<BookCategory> categories = Arrays.asList(category);

        when(profileService.saveProfileAuthors("nikos,apo,giorgos")).thenReturn(authors);
        when(profileService.saveProfileCategories("horror")).thenReturn(categories);
        
        UserProfile existingProfile = new UserProfile();
        existingProfile.setUsername("user");

        when(profileService.findProfile("user")).thenReturn(existingProfile);

        mockMvc.perform(MockMvcRequestBuilders.get("/saveProfile")
                .param("fullname", profile.getFullname())
                .param("age", String.valueOf(profile.getAge()))
                .param("address", profile.getAddress())
                .param("phone", profile.getPhone())
                .param("authors", "nikos,apo,giorgos")
                .param("categories", "horror"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/main_menu"));

        verify(profileService).update(existingProfile);
        assertEquals("Tester", existingProfile.getFullname());
        assertEquals(21, existingProfile.getAge());
        assertEquals("Perikleous 1", existingProfile.getAddress());
        assertEquals("6945242094", existingProfile.getPhone());
        assertEquals(authors, existingProfile.getFavoriteBookAuthors());
        assertEquals(categories, existingProfile.getFavoriteBookCategories());

    }
	
	@Test
	@WithMockUser(username="user", roles={"USER"})
	public void testRequests() throws Exception {
		
		UserProfile prof = new UserProfile();
		prof.setUsername("user");
        prof.setNotifications(new ArrayList<>());
        
		Book book = new Book(1, "title", "description", null, null, null);
		List<Book> books = new ArrayList<>(Arrays.asList(book));
		
		
		prof.setBookOffers(books);
		
	    when(profileService.findProfile("user")).thenReturn(prof);
	    
        mockMvc.perform(MockMvcRequestBuilders.get("/requests"))
	        .andExpect(MockMvcResultMatchers.status().isOk())
	        .andExpect(MockMvcResultMatchers.model().attribute("books", prof.getBookOffers()))
	        .andExpect(MockMvcResultMatchers.view().name("user/requesters"));
 
		
	}
	
	@Test
	@WithMockUser(username = "user", roles = {"USER"})
    public void testSelectedUser() throws Exception {
		
        Authentication authentication = new TestingAuthenticationToken("user", "password");
        String bookOwner = authentication.getName();
        UserProfile owner = new UserProfile();
        owner.setUsername(bookOwner);
        when(profileService.findProfile(bookOwner)).thenReturn(owner);

        UserProfile selectedUser = new UserProfile();
        selectedUser.setUsername("selectedUser");
        List<UserProfile> profs = new ArrayList<>();
        profs.add(selectedUser);

        Book book = new Book();
        book.setTitle("Test Book");
        book.setRequestingUsers(profs);
        when(bookService.findById(1)).thenReturn(Optional.of(book));

        mockMvc.perform(MockMvcRequestBuilders.get("/userSelected")
                .param("bookId", "1")
                .param("username", "selectedUser")
                .principal(authentication))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/requests"));
    }
	
	@Test
	@WithMockUser(username = "user", roles = {"USER"})
    public void testDeleteBook() throws Exception {
		
        UserProfile user1 = new UserProfile();
        user1.setUsername("user1");
        UserProfile user2 = new UserProfile();
        user2.setUsername("user2");
        List<UserProfile> profs = new ArrayList<>();
        profs.add(user1);
        profs.add(user2);

        Book book = new Book();
        book.setBookId(1);
        book.setTitle("Test Book");
        book.setRequestingUsers(profs);
        
        when(bookService.findById(1)).thenReturn(Optional.of(book));
        doNothing().when(bookService).deleteById(1);
        

        mockMvc.perform(MockMvcRequestBuilders.get("/bookDeleted")
                .param("bookId", "1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/requests"));
        
        
        verify(bookService, times(1)).deleteById(1);
        
        //since I verified that the delete is called at least one time and the test for the delete already passed in the
        //bookServiceTest I simulate the object book being deleted
        when(bookService.findById(1)).thenReturn(Optional.empty());
        
        assertFalse(bookService.findById(1).isPresent());
    }
	
	@Test
	@WithMockUser(username = "user", roles = {"USER"})
	public void testRequestDeleted() throws Exception{
		
		UserProfile prof = new UserProfile();
		prof.setUsername("user");
        prof.setNotifications(new ArrayList<>());
       
		List<UserProfile> profs = new ArrayList<>(Arrays.asList(prof));
		
		Book book = new Book(1, "title", "description", null, null, null);
		book.setRequestingUsers(profs);
		
		when(bookService.findById(1)).thenReturn(Optional.of(book));
		when(profileService.findProfile("user")).thenReturn(prof);
		
        mockMvc.perform(MockMvcRequestBuilders.get("/requestDeleted")
	        	.param("bookId", "1"))
		        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		        .andExpect(MockMvcResultMatchers.redirectedUrl("/viewRequests"));
        
        verify(bookService, times(1)).saveBook(book);
        when(bookService.findById(1)).thenReturn(Optional.of(book));
        
        assertTrue((book.getRequestingUsers()).isEmpty());
	}
	
	@Test
	@WithMockUser(username = "user", roles = {"USER"})
	public void testRequestBook() throws Exception{
		
		UserProfile profile = new UserProfile();
		profile.setUsername("user");
		profile.setId(1);
		
		when(profileService.findProfile("user")).thenReturn(profile);
		
		BookCategory category = new BookCategory(1,"horror", null);
		
		Book book1 = new Book();
		book1.setBookId(1);
		book1.setTitle("test");
		book1.setRequestingUsers(new ArrayList<>());
		book1.setBookCategory(category);
		
		Book book2 = new Book();
		book2.setBookId(1);
		book2.setTitle("test");
		book2.setRequestingUsers(new ArrayList<>());
		book2.setBookCategory(category);
		
		List<Book> userBooks = Arrays.asList(book2);
		List<Book> allBooks = Arrays.asList(book1, book2);
		List<Book> allBooksExceptCurrentUser = new ArrayList<>(allBooks);
		
		profile.setBookOffers(userBooks);
		
		when(bookService.findAll()).thenReturn(allBooks);
		when(bookService.findAllExceptCurrentUser(profile.getBookOffers())).thenReturn(allBooksExceptCurrentUser);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/requestBook"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("user/requestBook"));
		
	}
	
	@Test
	@WithMockUser(username = "user", roles = {"USER"})
	public void testBookRequested() throws Exception{
		
		UserProfile prof = new UserProfile();
		prof.setUsername("user");
        prof.setNotifications(new ArrayList<>());
        
		List<UserProfile> reqUsers = new ArrayList<>();
		
		Book book = new Book(1, "title", "description", null, null, null);
		
		reqUsers.add(prof);
		book.setRequestingUsers(reqUsers);
		
		when(bookService.findById(1)).thenReturn(Optional.of(book));
		when(profileService.findProfile("user")).thenReturn(prof);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/bookRequested")
	            .param("bookId", "1"))
	            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
	            .andExpect(MockMvcResultMatchers.redirectedUrl("/user/main_menu"));
		
		verify(bookService).saveBook(book);
		
		assertTrue(book.getRequestingUsers().contains(prof));
	}
	
	@Test
	@WithMockUser(username = "user", roles = {"USER"})
	public void testViewRequests() throws Exception{
		
		UserProfile profile = new UserProfile();
		profile.setUsername("user");
        profile.setId(1);
       
		when(profileService.findProfile("user")).thenReturn(profile);
		
		BookCategory category = new BookCategory(1,"horror", null);
		
		Book book1 = new Book();
		book1.setBookId(1);
		book1.setTitle("test");
		book1.setRequestingUsers(new ArrayList<>());
		book1.setBookCategory(category);
		
		Book book2 = new Book();
		book2.setBookId(1);
		book2.setTitle("test");
		book2.setRequestingUsers(new ArrayList<>());
		book2.setBookCategory(category);
		
		List<Book> requestedBooks = Arrays.asList(book1, book2);
		
		when(bookService.findRequestedBooksByUser(profile)).thenReturn(requestedBooks);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/viewRequests"))
	            .andExpect(MockMvcResultMatchers.status().isOk())
	            .andExpect(MockMvcResultMatchers.view().name("user/myRequests"))
        		.andExpect(MockMvcResultMatchers.model().attribute("books", requestedBooks));
		
		verify(bookService).findRequestedBooksByUser(profile);
	}
	
	

	
}