package ch.hearc.nde.bookservice.api.jms.deserialization;

public class JsonDeserialisationException extends Exception{


    public JsonDeserialisationException(String message, Exception e){
        super(message,e);
    }


}
