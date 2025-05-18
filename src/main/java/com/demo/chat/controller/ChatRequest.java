package com.demo.chat.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRequest {
    private String chatId;
    private String reqId;
    private String user;
    private String query;
    private Settings settings;
    private List<Message> messages;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Settings {
        private String model;
        private String systemInstruction;
        private Float temperature;
        private Float topP;
        private Integer topK;
        private Float repetitionPenalty;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Message {
        private String role;
        private String content;
    }
}