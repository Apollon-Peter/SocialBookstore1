package myy803.social_book_store.model;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name="book_categories")
public class BookCategory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="categoryId")
	private int categoryId;
	
	@Column(name = "name")
	private String name;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "category_of_book_id")
	private List<Book> books;
	
	public BookCategory(int categoryId, String name, List<Book> books) 
	{
        this.categoryId = categoryId;
        this.name = name;
        this.books = books;
    }
	
	public BookCategory() {
		
	}

    public int getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(int categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getName() 
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<Book> getBooks() 
    {
        return books;
    }

    public void setBooks(List<Book> books)
    {
        this.books = books;
    }
}
