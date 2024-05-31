package ch.hearc.nde.loanservice.remote;

import ch.hearc.nde.loanservice.exception.BookNotFound;
import ch.hearc.nde.loanservice.remote.model.BookResponse;

import java.util.Optional;

public interface BookServiceRemoteClient {
    BookResponse getBook(Long bookId) throws BookNotFound;
    void markAsLost(Long bookId) throws BookNotFound;
}
