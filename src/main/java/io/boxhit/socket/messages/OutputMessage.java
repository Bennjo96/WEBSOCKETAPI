package io.boxhit.socket.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

@JsonDeserialize(using = MessageDeserializer.class)
public class OutputMessage {

   private String module = "";
    private String header = "";
    private String json = "";

    public OutputMessage(){
    }

    public OutputMessage(String module, String header, String json) {
        this.module = module;
        this.header = header;
        this.json = json;
    }

    public OutputMessage(String module, Map<String, String> header, String json) throws JsonProcessingException {
        this.module = module;
        this.header = new ObjectMapper().writeValueAsString(header);
        this.json = json;
    }

    public OutputMessage(String module, String header, Map<String, String> json) throws JsonProcessingException {
        this.module = module;
        this.header = header;
        this.json = new ObjectMapper().writeValueAsString(json);
    }


    public OutputMessage(String module, Map<String, String> header, Map<String, String> json) throws JsonProcessingException {
        this.module = module;
        this.header = new ObjectMapper().writeValueAsString(header);
        this.json = new ObjectMapper().writeValueAsString(json);
    }

    public String getModule() {
        return module;
    }

    public OutputMessage setModule(String module) {
        this.module = module;
        return this;
    }

    public String getHeader() {
        return header;
    }

    public OutputMessage setHeader(String header) {
        this.header = header;
        return this;
    }

    public String getJson() {
        return json;
    }

    public OutputMessage setJson(String json) {
        this.json = json;
        return this;
    }

    @Override
    public String toString() {
        return "OutputMessage{" +
                "module=" + module +
                ", header=" + header +
                ", json=" + json +
                '}';
    }

}
