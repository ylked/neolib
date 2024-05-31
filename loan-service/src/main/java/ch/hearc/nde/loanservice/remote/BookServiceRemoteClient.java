package ch.hearc.nde.loanservice.remote;

import ch.hearc.nde.loanservice.remote.model.BookResponse;

import java.util.Optional;

public interface BookServiceRemoteClient {
    Optional<BookResponse> getBook(Long bookId);
    void markAsLost(Long bookId);
}
