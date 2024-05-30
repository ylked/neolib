package ch.hearc.nde.bookservice.api.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.jms.JMSException;

public interface JmsListener {
    void listenStatusChange(String jsonMessage) throws JMSException, JsonProcessingException;
}
