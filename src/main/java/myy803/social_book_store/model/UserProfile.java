package myy803.social_book_store.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name="profs")
public class UserProfile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="username")
	private String username;
	
	@Column(name="full_name")
	private String fullname;
	
	@Column(name="age")
	private int age;
	
	@Column(name="address")
	private String address;

	@Column(name="phone")
	private String phone;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE , CascadeType.PERSIST})
    @JoinTable(name = "favorite_authors",
            joinColumns = @JoinColumn(name = "user_name", referencedColumnName = "username"),
            inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "authorId"))
	private List<BookAuthor> favoriteBookAuthors;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE , CascadeType.PERSIST})
    @JoinTable(name = "favorite_categories",
            joinColumns = @JoinColumn(name = "user_name", referencedColumnName = "username"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "categoryId"))
	private List<BookCategory> favoriteBookCategories;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private List<Book> bookOffers;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private User user;
	
	@Column(name="notifications", length=1000)
	private List<String> notifications = new ArrayList();

	public UserProfile(int id, String username) {
		this.id = id;
		this.username = username;
		this.fullname = null;
		this.age = 0;
		this.address = null;
		this.phone = null;
		this.favoriteBookAuthors = null;
		this.favoriteBookCategories = null;
		this.bookOffers = null;
	}
	
	public UserProfile(int id, String username, String fullname, String address, int age, String phone_number) {
		this.id = id;
		this.username = username;
		this.fullname = fullname;
		this.address = address;
		this.age = age;
		this.phone = phone_number;
	}
	
	public UserProfile()
	{
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() 
	{
		return username;
	}
		
	public void setUsername(String username) 
	{
		this.username = username;
	}
		
	public String getFullname() 
	{
		return fullname;
	}
		
	public void setFullname(String fullname) 
	{
		this.fullname = fullname;
	}
		
	public int getAge()
	{
		return age;
	}
		
	public void setAge(int age) 
	{
		this.age = age;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
		
	public List<BookAuthor> getFavoriteBookAuthors() 
	{
		return favoriteBookAuthors;
	}
		
	public void setFavoriteBookAuthors(List<BookAuthor> favoriteBookAuthors)
	{
		this.favoriteBookAuthors = favoriteBookAuthors;
	}
		
	public List<BookCategory> getFavoriteBookCategories()
	{
		return favoriteBookCategories;
	}
		
	public void setFavoriteBookCategories(List<BookCategory> favoriteBookCategories) 
	{
		this.favoriteBookCategories = favoriteBookCategories;
	}
		
	public List<Book> getBookOffers() 
	{
		return bookOffers;
	}
		
	public void setBookOffers(List<Book> bookOffers) 
	{
		this.bookOffers = bookOffers;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public List<String> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<String> notifications) {
		this.notifications = notifications;
	}
	
}
