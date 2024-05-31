package ch.hearc.nde.loanservice.service.impl;

import ch.hearc.nde.loanservice.exception.*;
import ch.hearc.nde.loanservice.jms.JmsMessageProducer;
import ch.hearc.nde.loanservice.jms.model.BookBorrowedMessage;
import ch.hearc.nde.loanservice.jms.model.BookReturnedMessage;
import ch.hearc.nde.loanservice.remote.BookServiceRemoteClient;
import ch.hearc.nde.loanservice.remote.model.BookResponse;
import ch.hearc.nde.loanservice.repository.BookRepository;
import ch.hearc.nde.loanservice.repository.LoanRepository;
import ch.hearc.nde.loanservice.repository.UserRepository;
import ch.hearc.nde.loanservice.repository.entity.BookEntity;
import ch.hearc.nde.loanservice.repository.entity.LoanEntity;
import ch.hearc.nde.loanservice.repository.entity.UserEntity;
import ch.hearc.nde.loanservice.service.LoanService;
import ch.hearc.nde.loanservice.service.model.Book;
import ch.hearc.nde.loanservice.service.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Autowired
    private BookServiceRemoteClient restClient;

    @Autowired
    private JmsMessageProducer jmsMessageProducer;

    private final int MAX_BORROWED_BOOKS = 3;
    private final int MAX_BORROWED_DAYS = 30;

    private static final Logger LOGGER = LoggerFactory.getLogger(LoanServiceImpl.class);


    @Override
    public void borrowBook(Long bookId, Long userId) throws UnavailableBook, TooManyBorrowedBooks, UserNotFound, BookNotFound, JsonProcessingException {
        UserEntity user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        Optional<BookEntity> optionalBook = bookRepository.findById(bookId);
        BookEntity book = null;

        BookResponse bookResponse = restClient.getBook(bookId);

        if (optionalBook.isEmpty()) {
            book = new BookEntity();
            book.setId(bookResponse.id());
            book.setTitle(bookResponse.title());
            book.setAuthor(bookResponse.author());
            bookRepository.save(book);
        } else {
            book = optionalBook.get();
        }

        if(!bookResponse.status().equals("AVAILABLE")){
            LOGGER.info("Book is not available : " + bookResponse.status());
            throw new UnavailableBook();
        }

        if (borrowedBooks(userId).size() >= MAX_BORROWED_BOOKS) {
            throw new TooManyBorrowedBooks();
        }

        if (loanRepository.existsByBookIdAndEndDateIsNull(bookId)) {
            LOGGER.info("Book is already borrowed");
            throw new UnavailableBook();
        }

        LocalDateTime deadline = LocalDateTime.now().plusDays(MAX_BORROWED_DAYS);

        LoanEntity entity = new LoanEntity();
        entity.setBook(book);
        entity.setStartDate(LocalDateTime.now());
        entity.setDeadline(deadline);
        entity.setUser(user);

        loanRepository.save(entity);
        jmsMessageProducer.sendBookBorrowed(new BookBorrowedMessage(bookId));
    }

    @Override
    public void returnBook(Long bookId) throws AlreadyReturned, JsonProcessingException {
        LoanEntity entity = loanRepository.findByBookIdAndEndDateIsNull(bookId).orElseThrow(AlreadyReturned::new);
        entity.setEndDate(LocalDateTime.now());

        loanRepository.save(entity);
        jmsMessageProducer.sendBookReturned(new BookReturnedMessage(bookId));
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

    @Override
    public void markAsLost(Long bookId) throws BookNotFound, AlreadyReturned {
        LoanEntity entity = loanRepository.findByBookIdAndEndDateIsNull(bookId).orElseThrow(AlreadyReturned::new);
        entity.setEndDate(LocalDateTime.now());

        loanRepository.save(entity);
        restClient.markAsLost(bookId);
    }
}
