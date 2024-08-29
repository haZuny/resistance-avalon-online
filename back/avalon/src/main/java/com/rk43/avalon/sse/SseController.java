package com.rk43.avalon.sse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Controller
public class SseController {

    SseService sseService;

    @Autowired
    public SseController(SseService sseService) {
        this.sseService = sseService;
    }

    @GetMapping("/sse/{roomId}")
    public ResponseEntity<SseEmitter> connect(@PathVariable String roomId) throws IOException {
        return sseService.connect(roomId);
    }
}
