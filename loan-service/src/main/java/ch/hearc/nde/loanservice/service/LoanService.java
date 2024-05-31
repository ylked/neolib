package ch.hearc.nde.loanservice.service;

import ch.hearc.nde.loanservice.service.exception.*;
import ch.hearc.nde.loanservice.service.model.Book;
import ch.hearc.nde.loanservice.service.model.User;

import java.util.List;

public interface LoanService {
    void borrowBook(Long bookId, Long userId) throws UnavailableBook, TooManyBorrowedBooks, UserNotFound, BookNotFound;
    void returnBook(Long bookId) throws AlreadyReturned;
    User whoHas(Long bookId);
    List<Book> borrowedBooks(Long userId);
}
