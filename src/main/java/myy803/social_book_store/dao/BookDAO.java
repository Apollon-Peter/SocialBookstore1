package myy803.social_book_store.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import myy803.social_book_store.model.Book;
import myy803.social_book_store.model.UserProfile;

public interface BookDAO extends JpaRepository<Book, Integer> {
	Optional<Book> findByTitle(String title);
	Optional<Book> findById(int id);
	public List<Book> findAll();
	
    @Query("SELECT b FROM Book b WHERE b NOT IN :bookOffers")
    List<Book> findAllExceptSpecificId(@Param("bookOffers") List<Book> bookOffers);
    
    @Query("SELECT b FROM Book b JOIN b.requestingUsers u WHERE u.id = :userId")
    List<Book> findRequestedBooksByUserId(@Param("userId") int userId);
    
    @Query("SELECT DISTINCT b FROM Book b " +
            "JOIN b.authors a " +
            "JOIN b.bookCategory bc " +
            "JOIN UserProfile u " +
            "ON u.id = :userId " +
            "WHERE bc IN (SELECT c FROM BookCategory c JOIN u.favoriteBookCategories fc WHERE c.categoryId = fc.categoryId) " +
            "OR a IN (SELECT a FROM BookAuthor a JOIN u.favoriteBookAuthors fa WHERE a.authorId = fa.authorId) " +
            "AND b NOT IN :bookOffers")
    public List<Book> findAllBasedOnProfileExceptCurrentUser(@Param("bookOffers") List<Book> bookOffers, @Param("userId") int userId);
    
    @Query("SELECT DISTINCT b FROM Book b " +
            "JOIN b.authors a " +
            "JOIN b.bookCategory bc " +
            "JOIN UserProfile u " +
            "ON u.id = :userId " +
            "WHERE bc IN (SELECT c FROM BookCategory c JOIN u.favoriteBookCategories fc WHERE c.categoryId = fc.categoryId) " +
            "OR a IN (SELECT a FROM BookAuthor a JOIN u.favoriteBookAuthors fa WHERE a.authorId = fa.authorId)")
    List<Book> findAllBasedOnProfile(@Param("userId") int userId);
    
    
    
    @Query("SELECT DISTINCT b FROM Book b " +
            "JOIN b.authors a " +
            "JOIN UserProfile u " +
            "ON u.id = :userId " +
            "WHERE b.title IN (SELECT b.title FROM Book b WHERE b.title = :search) " +
            "OR a IN (SELECT a FROM BookAuthor a WHERE a.name = :search)")
	List<Book> findAllBasedOnSearch(@Param("userId") int userId, @Param("search") String search);
	
    @Query("SELECT DISTINCT b FROM Book b " +
            "JOIN b.authors a " +
            "JOIN UserProfile u " +
            "ON u.id = :userId " +
            "WHERE b.title IN (SELECT b.title FROM Book b WHERE b.title LIKE %:search%) " +
            "OR a IN (SELECT a FROM BookAuthor a WHERE a.name LIKE %:search%)")
    List<Book> approximateSearch(@Param("userId") int userId, @Param("search") String search);

	
	@Query("SELECT DISTINCT b FROM Book b " +
            "JOIN b.authors a " +
            "JOIN UserProfile u " +
            "ON u.id = :userId " +
            "WHERE b.title IN (SELECT b.title FROM Book b WHERE b.title = :search) " +
            "OR a IN (SELECT a FROM BookAuthor a WHERE a.name = :search) " +
    		"AND b NOT IN :bookOffers")
	List<Book> findAllBasedOnSearchExceptCurrentUser(@Param("bookOffers") List<Book> bookOffers, @Param("userId") int userId, @Param("search") String search);
	
	@Query("SELECT DISTINCT b FROM Book b " +
            "JOIN b.authors a " +
            "JOIN UserProfile u " +
            "ON u.id = :userId " +
            "WHERE b.title IN (SELECT b.title FROM Book b WHERE b.title LIKE %:search%) " +
            "OR a IN (SELECT a FROM BookAuthor a WHERE a.name LIKE %:search%) " +
			"AND b NOT IN :bookOffers")
	List<Book> approximateSearchExceptCurrentUser(@Param("bookOffers") List<Book> bookOffers, @Param("userId") int userId, @Param("search") String search);
}

