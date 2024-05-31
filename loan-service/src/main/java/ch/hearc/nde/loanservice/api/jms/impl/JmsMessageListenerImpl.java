package ch.hearc.nde.loanservice.api.jms.impl;


import ch.hearc.nde.loanservice.api.jms.JmsMessageListener;
import ch.hearc.nde.loanservice.api.jms.deserialization.BookUpdatedJmsDeserializerMapper;
import ch.hearc.nde.loanservice.api.jms.deserialization.JsonDeserialisationException;
import ch.hearc.nde.loanservice.api.jms.model.BookUpdatedMessage;
import ch.hearc.nde.loanservice.service.BookService;
import ch.hearc.nde.loanservice.service.exception.BookNotFound;
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
    private BookUpdatedJmsDeserializerMapper mapper;

    @Autowired
    private BookService service;

    private final Logger LOGGER = LoggerFactory.getLogger(JmsMessageListenerImpl.class);

    @Override
    @JmsListener(destination = "${spring.activemq.book.updated.queue}")
    public void listenBookUpdated(TextMessage jsonMessage) throws JMSException, JsonProcessingException {
        assert jsonMessage != null;

        String messageData = jsonMessage.getText();

        BookUpdatedMessage message = null;
        try {
            message = mapper.mapFromJson(messageData);
            service.updateBook(message.id(), message.title(), message.author());
            LOGGER.info("Book " + message.id() + " borrowed");
        } catch (JsonDeserialisationException e) {
            LOGGER.error("Could not deserialize the message", e);
        } catch (BookNotFound e) {
            LOGGER.warn("Book with id=" + message.id() + " has been updated but " +
                    "it does not exist in local database yet.", e);
        }
    }
}
