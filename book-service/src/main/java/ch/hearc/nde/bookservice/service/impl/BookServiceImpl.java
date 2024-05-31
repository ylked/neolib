package ch.hearc.nde.bookservice.service.impl;

import ch.hearc.nde.bookservice.jms.JmsMessageProducer;
import ch.hearc.nde.bookservice.jms.model.BookUpdatedMessage;
import ch.hearc.nde.bookservice.repository.BookRepository;
import ch.hearc.nde.bookservice.repository.entity.BookEntity;
import ch.hearc.nde.bookservice.common.BookStatus;
import ch.hearc.nde.bookservice.service.BookService;
import ch.hearc.nde.bookservice.service.exception.IllegalOperation;
import ch.hearc.nde.bookservice.service.exception.NotFound;
import ch.hearc.nde.bookservice.service.model.Book;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository repository;

    @Autowired
    private JmsMessageProducer jmsMessageProducer;

    @Override
    public Book create(String title, String author, String isbn) {
        BookEntity entity = new BookEntity(null, title, author, isbn, BookStatus.AVAILABLE);
        entity = repository.save(entity);
        return getBookFromEntity(entity);
    }

    @Override
    public List<Book> get() {
        return StreamSupport
                .stream(repository.findAll().spliterator(), false)
                .map(this::getBookFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Book get(Long id) throws NotFound {
        BookEntity entity = repository.findById(id).orElseThrow(NotFound::new);
        return getBookFromEntity(entity);
    }

    @Override
    public Book rename(Long id, String title) throws NotFound, JsonProcessingException {
        BookEntity entity = repository.findById(id).orElseThrow(NotFound::new);
        entity.setTitle(title);
        entity = repository.save(entity);

        jmsMessageProducer.sendBookUpdated(new BookUpdatedMessage(
                entity.getId(),
                entity.getTitle(),
                entity.getAuthor()
        ));

        return getBookFromEntity(entity);
    }

    @Override
    public Book changeAuthor(Long id, String author) throws NotFound, JsonProcessingException {
        BookEntity entity = repository.findById(id).orElseThrow(NotFound::new);
        entity.setAuthor(author);
        entity = repository.save(entity);

        jmsMessageProducer.sendBookUpdated(new BookUpdatedMessage(
                entity.getId(),
                entity.getTitle(),
                entity.getAuthor()
        ));

        return getBookFromEntity(entity);
    }

    @Override
    public Book changeIsbn(Long id, String isbn) throws NotFound {
        BookEntity entity = repository.findById(id).orElseThrow(NotFound::new);
        entity.setIsbn(isbn);
        entity = repository.save(entity);
        return getBookFromEntity(entity);
    }

    @Override
    public Book changeStatus(Long id, BookStatus status) throws NotFound {
        BookEntity entity = repository.findById(id).orElseThrow(NotFound::new);
        entity.setStatus(status);
        entity = repository.save(entity);
        return getBookFromEntity(entity);
    }

    @Override
    public void delete(Long id) throws NotFound, IllegalOperation {
        BookEntity entity = repository.findById(id).orElseThrow(NotFound::new);
        if(entity.getStatus() == BookStatus.BORROWED) {
            throw new IllegalOperation();
        }
        repository.delete(entity);
    }

    private Book getBookFromEntity(BookEntity entity) {
        return new Book(
                entity.getId(),
                entity.getTitle(),
                entity.getAuthor(),
                entity.getIsbn(),
                entity.getStatus()
        );
    }
}
