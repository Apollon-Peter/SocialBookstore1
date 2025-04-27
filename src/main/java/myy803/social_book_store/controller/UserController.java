package myy803.social_book_store.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import myy803.social_book_store.dao.BookDAO;
import myy803.social_book_store.dao.ProfileDAO;
import myy803.social_book_store.model.Book;
import myy803.social_book_store.model.User;
import myy803.social_book_store.model.UserProfile;
import myy803.social_book_store.service.AuthorService;
import myy803.social_book_store.service.ProfileService;
import myy803.social_book_store.service.UserService;
import myy803.social_book_store.service.BookService;
import myy803.social_book_store.service.CategoryService;


@Controller

public class UserController {
	@Autowired
    UserService userService;
	
	@Autowired
	ProfileDAO profileDAO;

	@Autowired
    ProfileService profileService;
	
	@Autowired
	AuthorService authorService;
	
	@Autowired
	BookService bookService;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	BookDAO bookDAO;
	
	@RequestMapping("/user/main_menu")
	public String getUserHome(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        model.addAttribute("username", username);
        
        UserProfile prof = profileService.findProfile(username);
        List<String> notifications = prof.getNotifications();
        Collections.reverse(notifications);
        model.addAttribute("notifications", notifications);
		return "/user/main_menu";
	}
	
	@RequestMapping("/logout")
	public String logout() {
		return "homepage";
	}
	
	@RequestMapping("/profile_details")
	public String createProfile(Authentication authentication, Model model) {
		authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserProfile prof = profileService.findProfile(username);
        model.addAttribute("authors", profileService.favAuthors(prof));
        model.addAttribute("categories", profileService.favCategories(prof));
        model.addAttribute("user", prof);
		return "user/profile";
	}
	
	@RequestMapping("/requests")
	public String requests(Authentication authentication, Model model) {
		authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserProfile prof = profileService.findProfile(username);
        //User user = userService.findById(username).get();
        model.addAttribute("books", prof.getBookOffers());
		return "user/requesters";
	}
	
	@RequestMapping("/userSelected")
	public String selectedUser(@RequestParam("bookId") int bookId, @RequestParam("username") String username, Authentication authentication) {
		Book book = bookService.findById(bookId).get();
		List<UserProfile> profs = book.getRequestingUsers();
		String message = "";
		for (UserProfile prof : profs) {
			if (prof.getUsername().equals(username)) {
				
				message = "You have been selected to receive the following book: ";
				
				authentication = SecurityContextHolder.getContext().getAuthentication();
				String bookOwner = authentication.getName();
				UserProfile owner = profileService.findProfile(bookOwner);
				List<String> ownerNotifs = new ArrayList();
				ownerNotifs.addAll(owner.getNotifications());
				ownerNotifs.add("You selected the user: '" + username + "' , to give him the book with title: '" + book.getTitle() + "'\n Contact Details: " + prof.getPhone() + " | " + prof.getAddress() + " | " + prof.getFullname());
				owner.setNotifications(ownerNotifs);
				profileService.saveProfile(owner);
				
			} else {
				message = "The following book is no longer available: ";
			}
			List<String> notifs = new ArrayList();
			notifs.addAll(prof.getNotifications());
			
			notifs.add(message + "'" + book.getTitle() + "'");
			prof.setNotifications(notifs);
			profileService.saveProfile(prof);
		}
		
		bookDAO.flush();
		bookService.deleteById(bookId);

		return "redirect:/requests";
	}
	
	@RequestMapping("/bookDeleted")
	public String deleteBook(@RequestParam("bookId") int bookId) {
		Book book = bookService.findById(bookId).get();
		List<UserProfile> profs = book.getRequestingUsers();
		for (UserProfile prof : profs) {
			List<String> notifs = new ArrayList();
			notifs.addAll(prof.getNotifications());
			notifs.add("The following book is no longer available: '" + book.getTitle() + "'");
			prof.setNotifications(notifs);
			profileService.saveProfile(prof);
		}
		
		bookDAO.flush();
		bookService.deleteById(bookId);
		
		return "redirect:/requests";
	}
	
	@RequestMapping("/requestDeleted")
	public String requestDeleted(@RequestParam("bookId") int bookId, Authentication authentication) {
		authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Book book = bookService.findById(bookId).get();
		List<UserProfile> profs = book.getRequestingUsers();
		UserProfile prof = profileService.findProfile(username);
		profs.remove(prof);
		book.setRequestingUsers(profs);
		bookService.saveBook(book);
		
		return "redirect:/viewRequests";
	}
	
	@RequestMapping("/saveProfile")
	public String saveProfile(@ModelAttribute("user") UserProfile prof,
							  @RequestParam("authors") String authors,
							  @RequestParam("categories") String categories,
							  Authentication authentication) {
        
		authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserProfile existingProfile = profileService.findProfile(username);
        
        existingProfile.setFullname(prof.getFullname());
        existingProfile.setAge(prof.getAge());
        existingProfile.setAddress(prof.getAddress());
        existingProfile.setPhone(prof.getPhone());
        existingProfile.setFavoriteBookAuthors(profileService.saveProfileAuthors(authors));
        existingProfile.setFavoriteBookCategories(profileService.saveProfileCategories(categories));
        
        profileService.update(existingProfile);
        
        return "redirect:/user/main_menu";

	}
	
	@RequestMapping("/offerBook")
	public String offerBook() {
		return "user/bookOffer";
	}
	
	@RequestMapping("/requestBook")
	public String requestBook(Model model, Authentication authentication) {
	    authentication = SecurityContextHolder.getContext().getAuthentication();
	    UserProfile prof = profileService.findProfile(authentication.getName());
	    List<Book> theBooks = new ArrayList<>();
	    
	    if (prof.getBookOffers().isEmpty()) {
	        theBooks = bookService.findAll();
	    } else {
	        theBooks = bookService.findAllExceptCurrentUser(prof.getBookOffers());
	    }
	    if (theBooks.isEmpty()) {
	        return "user/noBooks";
	    }
	    
	    List<Book> booksNotRequested = new ArrayList<>();
	    for (Book book : theBooks) {
	        boolean alreadyRequested = false;
	        for (UserProfile user : book.getRequestingUsers()) {
	            if (user.getId() == (prof.getId())) {
	                alreadyRequested = true;
	                break;
	            }
	        }
	        if (!alreadyRequested) {
	            booksNotRequested.add(book);
	        }
	    }
	    
	    if (booksNotRequested.isEmpty()) {
	        return "user/noBooks";
	    }
	    
	    model.addAttribute("books", booksNotRequested);
	    return "user/requestBook";
	}
	
	@RequestMapping("/bookRequested")
	public String  bookRequested(@RequestParam("bookId") int bookId, Authentication authentication) {
		authentication = SecurityContextHolder.getContext().getAuthentication();
		Book book = bookService.findById(bookId).get();
        String username = authentication.getName();
        UserProfile prof = profileService.findProfile(username);
        
        List<UserProfile> reqUsers = book.getRequestingUsers();
        reqUsers.add(prof);
        book.setRequestingUsers(reqUsers);
        bookService.saveBook(book);
        
		return "redirect:/user/main_menu";
	}
	
	@RequestMapping("/saveBook")
	public String saveBook(@RequestParam("title") String title,
						   @RequestParam("description") String description,
						   @RequestParam("author") String authors,
						   @RequestParam("category") String categories,
						   Authentication authentication) {
		
		authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserProfile prof = profileService.findProfile(username);
        
        Book book = bookService.saveBookDetails(title,authors,categories, description);

		bookService.saveBook(book);
		List<Book> offers = prof.getBookOffers();
		offers.add(book);
		prof.setBookOffers(offers);
		profileService.update(prof);

		return "redirect:/user/main_menu";
	}
	
	@RequestMapping("/viewRequests")
	public String viewRequests(Authentication authentication, Model model) {
		authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserProfile prof = profileService.findProfile(username);
        
        List<Book> requestedBooks = bookService.findRequestedBooksByUser(prof);
        
        model.addAttribute("books", requestedBooks);
		return "user/myRequests";
	}	
	
	@RequestMapping("/viewRecommended")
	public String viewRecommended(Authentication authentication ,Model model) {
		authentication = SecurityContextHolder.getContext().getAuthentication();
	    UserProfile prof = profileService.findProfile(authentication.getName());
	    List<Book> theBooks = new ArrayList<>();
	    
	    if (prof.getBookOffers().isEmpty()) {
	        theBooks = bookService.findAllBasedOnProfile(prof);
	    } else {
	        theBooks = bookService.findAllBasedOnProfileExceptCurrentUser(prof.getBookOffers(), prof);
	    }
	    if (theBooks.isEmpty()) {
	        return "user/noBooks";
	    }
	    
	    List<Book> booksNotRequested = new ArrayList<>();
	    for (Book book : theBooks) {
	        boolean alreadyRequested = false;
	        for (UserProfile user : book.getRequestingUsers()) {
	            if (user.getId() == (prof.getId())) {
	                alreadyRequested = true;
	                break;
	            }
	        }
	        if (!alreadyRequested) {
	            booksNotRequested.add(book);
	        }
	    }
	    
	    if (booksNotRequested.isEmpty()) {
	        return "user/noBooks";
	    }
	    
	    model.addAttribute("books", booksNotRequested);
		return "user/requestBook";
	}
	
	@RequestMapping("/search")
	public String searchBooks(Authentication authentication ,Model model,
							  @RequestParam("search") String search,
							  @RequestParam("search-strategy") String strategy) {
		authentication = SecurityContextHolder.getContext().getAuthentication();
	    UserProfile prof = profileService.findProfile(authentication.getName());
	    List<Book> theBooks = new ArrayList<>();

	    if(strategy.equals("exact")) {
		    if (prof.getBookOffers().isEmpty()) {
		        theBooks = bookService.exactSearch(prof,search);
		    } else {
		        theBooks = bookService.exactSearchExceptCurrentUser(prof.getBookOffers(), prof, search);
		    }
		    
	    }else if(strategy.equals("approximate")) {
	    	if (prof.getBookOffers().isEmpty()) {
	    		theBooks = bookService.approximateSearch(prof,search);
		    } else {
		        theBooks = bookService.approximateSearchExceptCurrentUser(prof.getBookOffers(), prof, search);
		    }
	    }
	    
	    if (theBooks.isEmpty()) {
	        return "user/noBooks";
	    }
	    
	    List<Book> booksNotRequested = new ArrayList<>();
	    for (Book book : theBooks) {
	        boolean alreadyRequested = false;
	        for (UserProfile user : book.getRequestingUsers()) {
	            if (user.getId() == (prof.getId())) {
	                alreadyRequested = true;
	                break;
	            }
	        }
	        if (!alreadyRequested) {
	            booksNotRequested.add(book);
	        }
	    }
	    
	    if (booksNotRequested.isEmpty()) {
	        return "user/noBooks";
	    }
	    model.addAttribute("books", booksNotRequested);
		return "user/requestBook";  
	}
	
}
