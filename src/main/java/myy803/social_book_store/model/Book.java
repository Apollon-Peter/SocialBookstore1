package myy803.social_book_store.model;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name="books")
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="bookId")
	private int bookId;
	
	@Column(name="title")
	private String title;
		
	@Column(name="description")
	private String description;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "authors",
			joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "bookId"),
			inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "authorId"))
	private List<BookAuthor> authors = new ArrayList();
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "category_of_book_id")
	private BookCategory bookCategory;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "requesting_users",
			joinColumns =  @JoinColumn(name = "book_id", referencedColumnName = "bookId"),
			inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "username"))
	private List<UserProfile> requestingUsers = new ArrayList();
	
	public Book(int bookId, String title, String description,  List<BookAuthor> bookAuthors,BookCategory bookCategory, List<UserProfile> requestingUsers)
	{
	    this.bookId = bookId;
	    this.title = title;
	    this.description = description;
	    this.authors = bookAuthors;
	    this.bookCategory = bookCategory;
	    this.requestingUsers = requestingUsers;
	}
	
	public Book() {
		
	}

	public int getBookId() 
	{
	    return bookId;
	}
	
	public void setBookId(int bookId)
	{
	    this.bookId = bookId;
	}
	
	public String getTitle() {
	    return title;
	}
	
	public void setTitle(String title)
	{
	    this.title = title;
	}
	
	public String getDescription() {
	    return description;
	}
	
	public void setDescription(String description)
	{
	    this.description = description;
	}
	
	public List<BookAuthor> getAuthors() 
	{
	    return authors;
	}
	
	public void setAuthors(List<BookAuthor> authors) 
	{
	    this.authors = authors;
	}
	
	public BookCategory getBookCategory() 
	{
	    return bookCategory;
	}
	
	public void setBookCategory(BookCategory bookCategory) 
	{
	    this.bookCategory = bookCategory;
	}
	
	public List<UserProfile> getRequestingUsers() 
	{
	    return requestingUsers;
	}
	
	public void setRequestingUsers(List<UserProfile> requestingUsers) 
	{
	    this.requestingUsers = requestingUsers;
	}
	
}
