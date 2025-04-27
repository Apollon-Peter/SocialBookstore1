package myy803.social_book_store.model;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name="book_authors")
public class BookAuthor {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="authorId")
	private int authorId;
	
	@Column(name="name")
	private String name;
	
	@ManyToMany(mappedBy = "authors")
	private List<Book> books = new ArrayList();
	
	public BookAuthor(int authorId, String name, List<Book> books) 
	{
        this.authorId = authorId;
        this.name = name;
        this.books = books;
    }
	
	public BookAuthor() {
		
	}

    public int getAuthorId() 
    {
        return authorId;
    }

    public void setAuthorId(int authorId)
    {
        this.authorId = authorId;
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
