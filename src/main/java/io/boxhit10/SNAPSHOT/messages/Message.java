package io.boxhit10.SNAPSHOT.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class Message {

    private String module;

    private String header;

    private String json;

    public Message(String module, String header, String json) {
        this.module = module;
        this.header = header;
        this.json = json;
    }

    public Message(MessageModule module, String header, String json) {
        this.module = module.getModule();
        this.header = header;
        this.json = json;
    }

    public Message(MessageModule module, Map<String, String> header, String json) throws JsonProcessingException {
        this.module = module.getModule();
        this.header = new ObjectMapper().writeValueAsString(header);
        this.json = json;
    }

    public Message(MessageModule module, String header, Map<String, String> json) throws JsonProcessingException {
        this.module = module.getModule();
        this.header = header;
        this.json = new ObjectMapper().writeValueAsString(json);
    }


    public Message(MessageModule module, Map<String, String> header, Map<String, String> json) throws JsonProcessingException {
        this.module = module.getModule();
        this.header = new ObjectMapper().writeValueAsString(header);
        this.json = new ObjectMapper().writeValueAsString(json);
    }

    public String getModule() {
        return module;
    }

    public Message setModule(String module) {
        this.module = module;
        return this;
    }

    public String getHeader() {
        return header;
    }

    public Message setHeader(String header) {
        this.header = header;
        return this;
    }

    public String getJson() {
        return json;
    }

    public Message setJson(String json) {
        this.json = json;
        return this;
    }
}
