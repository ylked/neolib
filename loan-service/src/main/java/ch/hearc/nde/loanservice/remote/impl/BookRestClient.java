package ch.hearc.nde.loanservice.remote.impl;

import ch.hearc.nde.loanservice.remote.model.BookBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@FeignClient(name = "book-service", configuration = FeignConfig.class)
public interface BookRestClient {

    @RequestMapping("/books/{bookId}")
    ResponseEntity<Optional<BookBody>> getBook(@PathVariable Long bookId);

    @PutMapping("/books/{bookId}/lost")
    ResponseEntity<?> markAsLost(@PathVariable Long bookId);
}