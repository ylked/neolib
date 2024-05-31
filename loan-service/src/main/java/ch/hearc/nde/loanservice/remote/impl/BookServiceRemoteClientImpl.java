package ch.hearc.nde.loanservice.remote.impl;

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
    public Optional<BookResponse> getBook(Long bookId) {
        Optional<BookBody> response = client.getBook(bookId).getBody();
        if (Objects.isNull(response)) {
            return Optional.empty();
        }
        return response.map(bookBody -> new BookResponse(
                bookBody.id(),
                bookBody.title(),
                bookBody.author(),
                bookBody.isbn(),
                bookBody.status()
        ));
    }

    @Override
    public void markAsLost(Long bookId) {
        client.markAsLost(bookId);
    }
}
