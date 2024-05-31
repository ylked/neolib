package ch.hearc.nde.loanservice.remote.impl;

import ch.hearc.nde.loanservice.exception.BookNotFound;
import ch.hearc.nde.loanservice.remote.BookServiceRemoteClient;
import ch.hearc.nde.loanservice.remote.model.BookBody;
import ch.hearc.nde.loanservice.remote.model.BookResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class BookServiceRemoteClientImpl implements BookServiceRemoteClient {
    @Autowired
    private BookRestClient client;

    @Override
    public BookResponse getBook(Long bookId) throws BookNotFound {
        try {
            Optional<BookBody> response = client.getBook(bookId).getBody();
            if (Objects.isNull(response) || response.isEmpty()) {
                throw new BookNotFound();
            }

            return response.map(bookBody -> new BookResponse(
                    bookBody.id(),
                    bookBody.title(),
                    bookBody.author(),
                    bookBody.isbn(),
                    bookBody.status()
            )).get();
        } catch (Exception e) {
            throw new BookNotFound();
        }

    }

    @Override
    public void markAsLost(Long bookId) {
        client.markAsLost(bookId);
    }
}
