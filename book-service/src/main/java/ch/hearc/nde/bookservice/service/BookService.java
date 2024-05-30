package ch.hearc.nde.bookservice.service;

import ch.hearc.nde.bookservice.common.BookStatus;
import ch.hearc.nde.bookservice.service.exception.*;
import ch.hearc.nde.bookservice.service.model.Book;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BookService {
    Book create(String title, String author, String isbn);
    List<Book> get();
    Book get(Long id) throws NotFound;
    Book rename(Long id, String title) throws NotFound;
    Book changeAuthor(Long id, String author) throws NotFound;
    Book changeIsbn(Long id, String isbn) throws NotFound;
    Book changeStatus(Long id, BookStatus status) throws NotFound;
    void delete(Long id) throws NotFound, IllegalOperation;
}
