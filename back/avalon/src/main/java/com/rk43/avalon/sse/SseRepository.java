package com.rk43.avalon.sse;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Optional;

@Repository
public class SseRepository {
    HashMap<String, SseEmitter> sseMap = new HashMap<>();

    public void create(String id){
        sseMap.put(id, new SseEmitter(1000L * 60 * 60));
    }

    public Optional<SseEmitter> find(String id){
        if (sseMap.containsKey(id)) return Optional.of(sseMap.get(id));
        return Optional.empty();
    }

    public boolean isExist(String id){
        return sseMap.containsKey(id);
    }
}
