package ch.hearc.nde.bookservice.api.jms.impl;

import ch.hearc.nde.bookservice.api.jms.JmsMessageListener;
import ch.hearc.nde.bookservice.api.jms.deserialization.BorrowJmsDeserializerMapper;
import ch.hearc.nde.bookservice.api.jms.deserialization.JsonDeserialisationException;
import ch.hearc.nde.bookservice.api.jms.deserialization.ReturnedJmsDeserializerMapper;
import ch.hearc.nde.bookservice.api.jms.model.BookBorrowedMessage;
import ch.hearc.nde.bookservice.api.jms.model.BookReturnedMessage;
import ch.hearc.nde.bookservice.common.BookStatus;
import ch.hearc.nde.bookservice.service.BookService;
import ch.hearc.nde.bookservice.service.exception.NotFound;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JmsMessageListenerImpl implements JmsMessageListener {
    @Autowired
    private BorrowJmsDeserializerMapper borrowMapper;

    @Autowired
    private ReturnedJmsDeserializerMapper returnedMapper;

    @Autowired
    private BookService service;

    private final Logger LOGGER = LoggerFactory.getLogger(JmsMessageListenerImpl.class);

    @Override
    @JmsListener(destination = "${spring.activemq.book.borrowed.queue}")
    public void listenBookBorrowed(TextMessage jsonMessage) throws JMSException, JsonProcessingException {
        assert jsonMessage != null;

        String messageData = jsonMessage.getText();

        BookBorrowedMessage message = null;
        try {
            message = borrowMapper.mapFromJson(messageData);
            service.changeStatus(message.bookId(), BookStatus.BORROWED);
            LOGGER.info("Book " + message.bookId() + " borrowed");
        } catch (JsonDeserialisationException e) {
            LOGGER.error("Could not deserialize the message", e);
        } catch (NotFound e) {
            LOGGER.error("Book " + message.bookId() + " not found");
        }
    }

    @Override
    @JmsListener(destination = "${spring.activemq.book.returned.queue}")
    public void listBookReturned(TextMessage jsonMessage) throws JMSException, JsonProcessingException {
        assert jsonMessage != null;

        String messageData = jsonMessage.getText();

        BookReturnedMessage message = null;
        try {
            message = returnedMapper.mapFromJson(messageData);
            service.changeStatus(message.bookId(), BookStatus.AVAILABLE);
            LOGGER.info("Book " + message.bookId() + " returned");
        } catch (JsonDeserialisationException e) {
            LOGGER.error("Could not deserialize the message", e);
        } catch (NotFound e) {
            LOGGER.error("Book " + message.bookId() + " not found");
        }
    }
}
