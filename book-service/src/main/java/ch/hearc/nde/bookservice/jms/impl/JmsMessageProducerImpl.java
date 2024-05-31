package ch.hearc.nde.bookservice.jms.impl;

import ch.hearc.nde.bookservice.jms.JmsMessageProducer;
import ch.hearc.nde.bookservice.jms.model.BookUpdatedMessage;
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

    @Value("${spring.activemq.book.updated.queue}")
    String bookUpdatedQueue;

    @Autowired
    JmsTemplate jmsTemplate;

    @Override
    public void sendBookUpdated(BookUpdatedMessage message) throws JsonProcessingException {
        String jsonObj = sendMessage(
                new ObjectMapper()
                        .writer()
                        .withDefaultPrettyPrinter()
                        .writeValueAsString(message),
                bookUpdatedQueue);
        LOGGER.info("Message send to queue: " + bookUpdatedQueue + ", message: " + jsonObj);
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
