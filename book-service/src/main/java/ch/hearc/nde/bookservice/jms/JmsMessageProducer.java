package ch.hearc.nde.bookservice.jms;

import ch.hearc.nde.bookservice.jms.model.BookUpdatedMessage;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface JmsMessageProducer {
    void sendBookUpdated(BookUpdatedMessage message) throws JsonProcessingException;

}
