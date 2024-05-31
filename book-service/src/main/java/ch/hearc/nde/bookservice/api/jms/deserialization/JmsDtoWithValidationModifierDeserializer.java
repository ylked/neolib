package ch.hearc.nde.bookservice.api.jms.deserialization;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;

/**
 * Configuration du processus de deserialisation avec le deserialiseur voulu (jms)
 */
public class JmsDtoWithValidationModifierDeserializer extends BeanDeserializerModifier {

    @Override
    public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription description,JsonDeserializer<?> deserializer){

        if(deserializer instanceof BeanDeserializer){
            return new JmsDtoWithValidationDeserializer((BeanDeserializer) deserializer);
        }

        return deserializer;
    }

}
