package ch.hearc.nde.loanservice.service;

import ch.hearc.nde.loanservice.exception.BookNotFound;
import ch.hearc.nde.loanservice.service.model.Book;

public interface BookService {
    Book createBook(String title, String author);
    Book getBook(Long id) throws BookNotFound;
    Book updateBook(Long id, String title, String author) throws BookNotFound;
    void deleteBook(Long id) throws BookNotFound;
}
