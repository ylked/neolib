package ch.hearc.nde.bookservice.api.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;

public interface JmsMessageListener {
    void listenBookBorrowed(TextMessage jsonMessage) throws JMSException, JsonProcessingException;
    void listBookReturned(TextMessage jsonMessage) throws JMSException, JsonProcessingException;
}
