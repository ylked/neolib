package ch.hearc.nde.loanservice.service.impl;

import ch.hearc.nde.loanservice.repository.BookRepository;
import ch.hearc.nde.loanservice.repository.LoanRepository;
import ch.hearc.nde.loanservice.repository.UserRepository;
import ch.hearc.nde.loanservice.repository.entity.BookEntity;
import ch.hearc.nde.loanservice.repository.entity.LoanEntity;
import ch.hearc.nde.loanservice.repository.entity.UserEntity;
import ch.hearc.nde.loanservice.service.LoanService;
import ch.hearc.nde.loanservice.service.exception.*;
import ch.hearc.nde.loanservice.service.model.Book;
import ch.hearc.nde.loanservice.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LoanServiceImpl implements LoanService {
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    private final int MAX_BORROWED_BOOKS = 3;
    private final int MAX_BORROWED_DAYS = 30;


    @Override
    public void borrowBook(Long bookId, Long userId) throws UnavailableBook, TooManyBorrowedBooks, UserNotFound, BookNotFound {
        UserEntity user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        Optional<BookEntity> optionalBook = bookRepository.findById(bookId);
        BookEntity book = null;

        if (optionalBook.isEmpty()) {
            //TODO fetch book from book service with REST, then save it in local db
        } else {
            book = optionalBook.get();
        }

        //TODO check that book can be borrowed on book service, with REST

        if (borrowedBooks(userId).size() >= MAX_BORROWED_BOOKS) {
            throw new TooManyBorrowedBooks();
        }

        if (loanRepository.existsByBookIdAndEndDateIsNull(bookId)) {
            throw new UnavailableBook();
        }

        LocalDateTime deadline = LocalDateTime.now().plusDays(MAX_BORROWED_DAYS);

        LoanEntity entity = new LoanEntity();
        entity.setBook(book);
        entity.setStartDate(LocalDateTime.now());
        entity.setDeadline(deadline);
        entity.setUser(user);

        loanRepository.save(entity);

        //TODO post JMS message
    }

    @Override
    public void returnBook(Long bookId) throws AlreadyReturned {
        LoanEntity entity = loanRepository.findByBookIdAndEndDateIsNull(bookId).orElseThrow(AlreadyReturned::new);
        entity.setEndDate(LocalDateTime.now());
        loanRepository.save(entity);

        //TODO post JMS message
    }

    @Override
    public User whoHas(Long bookId) {
        return loanRepository
                .findByBookIdAndEndDateIsNull(bookId)
                .map((LoanEntity loan) -> {
                    UserEntity user = loan.getUser();
                    return new User(
                            user.getId(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getEmail(),
                            user.getCardNumber()
                    );
                })
                .orElse(null);
    }

    @Override
    public List<Book> borrowedBooks(Long userId) {
        return loanRepository
                .findByUserIdAndEndDateIsNull(userId)
                .stream()
                .map((LoanEntity loan) -> {
                    BookEntity book = loan.getBook();
                    return new Book(
                            book.getId(),
                            book.getTitle(),
                            book.getAuthor()
                    );
                })
                .toList();
    }
}
