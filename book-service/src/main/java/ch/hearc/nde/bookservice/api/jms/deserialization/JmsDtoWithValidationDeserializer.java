package ch.hearc.nde.bookservice.api.jms.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import jakarta.validation.*;

import java.io.IOException;
import java.util.Set;

/**
 * Classe de configuration du mappiong json en objet
 */
public class JmsDtoWithValidationDeserializer extends BeanDeserializer {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    protected JmsDtoWithValidationDeserializer(BeanDeserializerBase src) {
        super(src);
    }

    <T> void validate(T t){
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        if(!violations.isEmpty()){
            throw new ConstraintViolationException(violations);
        }
    }
    @Override
    public Object deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        Object instance = super.deserialize(parser,context);
        validate(instance);
        return instance;

    }
}
