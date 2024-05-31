package ch.hearc.nde.loanservice.api.web;

import ch.hearc.nde.loanservice.api.web.dto.request.NewLoanRequestDTO;
import ch.hearc.nde.loanservice.api.web.dto.request.BookIdRequestDTO;
import ch.hearc.nde.loanservice.api.web.dto.response.BookResponseDTO;
import ch.hearc.nde.loanservice.api.web.dto.response.StringMessage;
import ch.hearc.nde.loanservice.api.web.dto.response.UserResponseDTO;
import ch.hearc.nde.loanservice.exception.*;
import ch.hearc.nde.loanservice.service.LoanService;
import ch.hearc.nde.loanservice.service.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loans")
public class LoanController {
    @Autowired
    private LoanService service;

    @PostMapping("/borrow")
    public @ResponseBody ResponseEntity<?> borrow(@RequestBody NewLoanRequestDTO dto) {
        try {
            service.borrowBook(dto.bookId(), dto.userId());
            return ResponseEntity.ok().build();
        } catch (UnavailableBook e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new StringMessage("Book is not available"));
        } catch (TooManyBorrowedBooks e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new StringMessage("User has too many borrowed books"));
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StringMessage("User not found"));
        } catch (BookNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StringMessage("Book not found"));
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StringMessage("Internal server error"));
        }
    }

    @PostMapping("/return")
    public @ResponseBody ResponseEntity<?> returnBook(@RequestBody BookIdRequestDTO dto) {
        try {
            service.returnBook(dto.bookId());
            return ResponseEntity.ok().build();
        } catch (AlreadyReturned e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StringMessage("Book has already been returned or does not exist"));
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StringMessage("Internal server error"));
        }
    }

    @GetMapping("/who-has/{bookId}")
    public @ResponseBody ResponseEntity<?> whoHas(@PathVariable Long bookId) {
        User user = service.whoHas(bookId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StringMessage("Book not found or currently not borrowed"));
        }
        return ResponseEntity.ok(
                new UserResponseDTO(
                        user.id(),
                        user.firstName(),
                        user.lastName(),
                        user.email(),
                        user.cardNumber()
                )
        );
    }

    @GetMapping("/borrowed-books/{userId}")
    public @ResponseBody ResponseEntity<?> borrowedBooks(@PathVariable Long userId) {
        return ResponseEntity.ok(
                service.borrowedBooks(userId)
                        .stream()
                        .map(book -> new BookResponseDTO(
                                book.id(),
                                book.title(),
                                book.author()
                        ))
                        .toList()
        );
    }

    @PutMapping("/lost/{bookId}")
    public @ResponseBody ResponseEntity<?> markAsLost(@PathVariable Long bookId) {
        try {
            service.markAsLost(bookId);
            return ResponseEntity.ok().build();
        } catch (BookNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StringMessage("Book not found"));
        } catch (AlreadyReturned e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new StringMessage("Book has already been" +
                    " returned or does not exist"));
        }
    }
}
