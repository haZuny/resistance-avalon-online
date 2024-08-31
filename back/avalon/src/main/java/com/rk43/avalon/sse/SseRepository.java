package com.rk43.avalon.sse;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@Repository
public class SseRepository {
    HashMap<String, ArrayList<SseEmitter>> sseMap = new HashMap<>();

    public void create(String id){
        ArrayList<SseEmitter> sseList = new ArrayList<>();
        sseMap.put(id, sseList);
    }

    public Optional<ArrayList<SseEmitter>> find(String id){
        if (sseMap.containsKey(id)) return Optional.of(sseMap.get(id));
        return Optional.empty();
    }

    public boolean isExist(String id){
        return sseMap.containsKey(id);
    }
}
