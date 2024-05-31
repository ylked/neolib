package ch.hearc.nde.loanservice.service;

import ch.hearc.nde.loanservice.exception.*;
import ch.hearc.nde.loanservice.service.model.Book;
import ch.hearc.nde.loanservice.service.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface LoanService {
    void borrowBook(Long bookId, Long userId) throws UnavailableBook, TooManyBorrowedBooks, UserNotFound, BookNotFound, JsonProcessingException;
    void returnBook(Long bookId) throws AlreadyReturned, JsonProcessingException;
    User whoHas(Long bookId);
    List<Book> borrowedBooks(Long userId);
    void markAsLost(Long bookId) throws BookNotFound, AlreadyReturned;
}
