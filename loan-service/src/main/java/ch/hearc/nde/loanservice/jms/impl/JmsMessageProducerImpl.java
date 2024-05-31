package ch.hearc.nde.loanservice.jms.impl;

import ch.hearc.nde.loanservice.jms.JmsMessageProducer;
import ch.hearc.nde.loanservice.jms.model.BookBorrowedMessage;
import ch.hearc.nde.loanservice.jms.model.BookReturnedMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsMessageProducerImpl implements JmsMessageProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(JmsMessageProducerImpl.class);

    @Value("${spring.activemq.book.borrowed.queue}")
    String bookBorrowedQueue;

    @Value("${spring.activemq.book.returned.queue}")
    String bookReturnedQueue;

    @Autowired
    JmsTemplate jmsTemplate;

    @Override
    public void sendBookBorrowed(BookBorrowedMessage message) throws JsonProcessingException {
        String jsonObj = sendMessage(
                new ObjectMapper()
                        .writer()
                        .withDefaultPrettyPrinter()
                        .writeValueAsString(message),
                bookBorrowedQueue);
        LOGGER.info("Message send to queue: " + bookBorrowedQueue + ", message: " + jsonObj);
    }

    @Override
    public void sendBookReturned(BookReturnedMessage message) throws JsonProcessingException {
        String jsonObj = sendMessage(
                new ObjectMapper()
                        .writer()
                        .withDefaultPrettyPrinter()
                        .writeValueAsString(message),
                bookReturnedQueue);
        LOGGER.info("Message send to queue: " + bookReturnedQueue + ", message: " + jsonObj);
    }

    private String sendMessage(String jsonObj, String destination) throws JsonProcessingException {

        jmsTemplate.send(destination, messageCreator -> {
            TextMessage message = messageCreator.createTextMessage();
            message.setText(jsonObj);
            return message;
        });
        return jsonObj;
    }
}
