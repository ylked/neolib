package ch.hearc.nde.bookservice.repository.entity;

import ch.hearc.nde.bookservice.common.BookStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "book")
public class BookEntity{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String title;
    private String author;
    private String isbn;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    public BookEntity() {
    }

    public BookEntity(Long id, String title, String author, String isbn, BookStatus status) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }
}
