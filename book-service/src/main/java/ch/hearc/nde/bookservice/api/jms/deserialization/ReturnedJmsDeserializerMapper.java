package ch.hearc.nde.bookservice.api.jms.deserialization;

import ch.hearc.nde.bookservice.api.jms.model.BookReturnedMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jakarta.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;

@Component
public class ReturnedJmsDeserializerMapper {

    private static ObjectMapper getObjectMapperWithValidation(){
        SimpleModule validationModule = new SimpleModule();
        validationModule.setDeserializerModifier(new JmsDtoWithValidationModifierDeserializer());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(validationModule);
        return mapper;
    }
    public BookReturnedMessage mapFromJson(String json) throws JsonDeserialisationException {
        try{
            ObjectMapper mapper = getObjectMapperWithValidation();
            return mapper.readValue(json, BookReturnedMessage.class);
        }catch(ConstraintViolationException | JsonProcessingException e){
            throw new JsonDeserialisationException(e.getMessage(),e);
        }
    }
}
