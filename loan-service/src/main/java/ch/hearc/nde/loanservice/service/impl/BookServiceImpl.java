package ch.hearc.nde.loanservice.service.impl;

import ch.hearc.nde.loanservice.repository.BookRepository;
import ch.hearc.nde.loanservice.repository.entity.BookEntity;
import ch.hearc.nde.loanservice.service.BookService;
import ch.hearc.nde.loanservice.exception.BookNotFound;
import ch.hearc.nde.loanservice.service.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository repository;

    @Override
    public Book createBook(String title, String author) {
        BookEntity entity = new BookEntity();
        entity.setTitle(title);
        entity.setAuthor(author);
        entity = repository.save(entity);
        return getBookFromEntity(entity);
    }

    @Override
    public Book getBook(Long id) throws BookNotFound {
        return repository.findById(id)
                .map(this::getBookFromEntity)
                .orElseThrow(BookNotFound::new);
    }

    @Override
    public Book updateBook(Long id, String title, String author) throws BookNotFound {
        BookEntity entity = repository.findById(id).orElseThrow(BookNotFound::new);

        if(title != null) {
            entity.setTitle(title);
        }

        if(author != null) {
            entity.setAuthor(author);
        }

        entity = repository.save(entity);
        return getBookFromEntity(entity);
    }

    @Override
    public void deleteBook(Long id) throws BookNotFound {
        BookEntity entity = repository.findById(id).orElseThrow(BookNotFound::new);
        repository.delete(entity);
    }

    private Book getBookFromEntity(BookEntity entity) {
        return new Book(
                entity.getId(),
                entity.getTitle(),
                entity.getAuthor()
        );
    }

}
