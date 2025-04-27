package myy803.social_book_store.service;

import java.util.List;

import org.springframework.stereotype.Service;

import myy803.social_book_store.model.BookAuthor;
import myy803.social_book_store.model.BookCategory;
import myy803.social_book_store.model.UserProfile;

@Service
public interface ProfileService {
	public void saveProfile(UserProfile prof);
	public void update(UserProfile prof);
	public UserProfile findProfile(String username);
	public List<BookAuthor> saveProfileAuthors(String authors);
	public List<BookCategory> saveProfileCategories(String categories);
	public String favAuthors(UserProfile prof);
	public String favCategories(UserProfile prof);
}
