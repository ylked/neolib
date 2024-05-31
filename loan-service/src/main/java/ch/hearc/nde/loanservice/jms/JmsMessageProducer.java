package ch.hearc.nde.loanservice.jms;

import ch.hearc.nde.loanservice.jms.model.BookBorrowedMessage;
import ch.hearc.nde.loanservice.jms.model.BookReturnedMessage;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface JmsMessageProducer {
    void sendBookBorrowed(BookBorrowedMessage message) throws JsonProcessingException;

    void sendBookReturned(BookReturnedMessage message) throws JsonProcessingException;
}
