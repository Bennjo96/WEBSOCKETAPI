package io.boxhit.socket.messages;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.logging.Logger;

public class MessageDeserializer extends JsonDeserializer<OutputMessage> {
    @Override
    public OutputMessage deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        ObjectCodec oc = p.getCodec();
        JsonNode node = oc.readTree(p);
        OutputMessage msg = null;
        try {
            msg = new OutputMessage(node.get("module").toString(), node.get("header").toString(), node.get("json").toString());
        }catch (Exception e){
            Logger.getLogger("MessageDeserializer").severe("Error while deserializing incoming message: " + e.getMessage());
        }
        return msg;
    }
}