package ch.hearc.nde.loanservice.api.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;

public interface JmsMessageListener {
    void listenBookUpdated(TextMessage jsonMessage) throws JMSException, JsonProcessingException;
}
