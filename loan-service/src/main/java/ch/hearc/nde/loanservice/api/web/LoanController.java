package ch.hearc.nde.loanservice.api.web;

import ch.hearc.nde.loanservice.api.web.dto.request.NewLoanRequestDTO;
import ch.hearc.nde.loanservice.api.web.dto.request.ReturnBookRequestDTO;
import ch.hearc.nde.loanservice.api.web.dto.response.BookResponseDTO;
import ch.hearc.nde.loanservice.api.web.dto.response.StringMessage;
import ch.hearc.nde.loanservice.api.web.dto.response.UserResponseDTO;
import ch.hearc.nde.loanservice.service.LoanService;
import ch.hearc.nde.loanservice.service.exception.*;
import ch.hearc.nde.loanservice.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loan")
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
        }
    }

    @PostMapping("/return")
    public @ResponseBody ResponseEntity<?> returnBook(@RequestBody ReturnBookRequestDTO dto) {
        try {
            service.returnBook(dto.bookId());
            return ResponseEntity.ok().build();
        } catch (AlreadyReturned e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StringMessage("Book has already been returned or does not exist"));
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
}
