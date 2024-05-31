package ch.hearc.nde.loanservice.repository.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class BookEntity {

    @Id
    private Long id;

    private String title;

    private String author;


    @OneToMany(mappedBy = "book")
    private List<LoanEntity> loans;

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

    public List<LoanEntity> getLoans() {
        return loans;
    }

    public void setLoans(List<LoanEntity> loans) {
        this.loans = loans;
    }
}
