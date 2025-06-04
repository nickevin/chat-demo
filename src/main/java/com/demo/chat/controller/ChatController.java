package com.demo.chat.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class ChatController {

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @PostMapping("/chat/streaming")
    public SseEmitter streaming(@RequestBody ChatRequest request) {
        log.debug("{}", request);

        SseEmitter emitter = new SseEmitter();
        executorService.execute(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500);

                if (request.getSettings().isReasoning()) {
                    emitter.send("{\"delta\":\"<think>\",\"status\":\"delta\"}");
                    emitter.send("{\"delta\":\"我\",\"status\":\"delta\"}");
                    emitter.send("{\"delta\":\"在\",\"status\":\"delta\"}");
                    emitter.send("{\"delta\":\"思考\",\"status\":\"delta\"}");
                    emitter.send("{\"delta\":\"中\",\"status\":\"delta\"}");
                    emitter.send("{\"delta\":\".\",\"status\":\"delta\"}");
                    emitter.send("{\"delta\":\".\",\"status\":\"delta\"}");
                    emitter.send("{\"delta\":\".\",\"status\":\"delta\"}");
                    emitter.send("{\"delta\":\"</think>\",\"status\":\"delta\"}");
                }
                emitter.send("{\"delta\":\"[流式]\",\"status\":\"delta\"}");
                emitter.send("{\"delta\":\"hello\",\"status\":\"delta\"}");
                TimeUnit.MILLISECONDS.sleep(250);
                emitter.send("{\"delta\":\" world\",\"status\":\"delta\"}");
                TimeUnit.MILLISECONDS.sleep(250);
                emitter.send("{\"response\":\"hello world" + "\",\"status\":\"finish\"}");
                TimeUnit.MILLISECONDS.sleep(250);
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) throws InterruptedException {
        log.debug("{}", request);

        TimeUnit.MILLISECONDS.sleep(800);
        String result = "";
        ChatResponse response = new ChatResponse();
        if (request.getSettings().isReasoning()) {
            result = "<think>我在思考中...</think>";
        }
        result += "[非流式]你好啊!";
        response.setResponse(result);
        return response;
    }


    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ChatRequest {
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
            @JsonProperty("isStreaming")
            private boolean isStreaming;
            @JsonProperty("isReasoning")
            private boolean isReasoning;
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

    @Data
    public static class ChatResponse {
        private String response;
    }
}