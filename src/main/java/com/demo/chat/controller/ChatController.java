package com.demo.chat.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class ChatController {

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @PostMapping("/chat/sse")
    public SseEmitter sse(@RequestBody ChatRequest request) {
        log.error("{}", request);

        SseEmitter emitter = new SseEmitter();
        executorService.execute(() -> {
            try {
                emitter.send("{\"delta\":\"hello\",\"status\":\"delta\"}");
                TimeUnit.MILLISECONDS.sleep(250);
                emitter.send("{\"delta\":\" world\",\"status\":\"delta\"}");
                TimeUnit.MILLISECONDS.sleep(250);
                emitter.send("{\"delta\":\" " + LocalDateTime.now() + "\",\"status\":\"delta\"}");
                TimeUnit.MILLISECONDS.sleep(250);
                emitter.send("{\"response\":\"hello world" + LocalDateTime.now() + "\",\"status\":\"finish\"}");
                TimeUnit.MILLISECONDS.sleep(250);
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        log.error("{}", request);

        ChatResponse response = new ChatResponse();
        response.setResponse("hello world " + LocalDateTime.now());
        response.setStatus("finish");
        return response;
    }
}

@Data
class ChatResponse {
    private String status;
    private String response;
}