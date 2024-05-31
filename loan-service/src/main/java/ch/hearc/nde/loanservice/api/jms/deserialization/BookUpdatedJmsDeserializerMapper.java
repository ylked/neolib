package ch.hearc.nde.loanservice.api.jms.deserialization;

import ch.hearc.nde.loanservice.api.jms.model.BookUpdatedMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jakarta.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;

@Component
public class BookUpdatedJmsDeserializerMapper {

    private static ObjectMapper getObjectMapperWithValidation(){
        SimpleModule validationModule = new SimpleModule();
        validationModule.setDeserializerModifier(new JmsDtoWithValidationModifierDeserializer());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(validationModule);
        return mapper;
    }
    public BookUpdatedMessage mapFromJson(String json) throws JsonDeserialisationException {
        try{
            ObjectMapper mapper = getObjectMapperWithValidation();
            return mapper.readValue(json, BookUpdatedMessage.class);
        }catch(ConstraintViolationException | JsonProcessingException e){
            throw new JsonDeserialisationException(e.getMessage(),e);
        }
    }
}
