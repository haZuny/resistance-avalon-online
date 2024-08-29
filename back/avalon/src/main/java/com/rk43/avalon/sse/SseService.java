package com.rk43.avalon.sse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Optional;

@Service
public class SseService {

    SseRepository sseRepository;

    @Autowired
    public SseService(SseRepository emiterRepository) {
        this.sseRepository = emiterRepository;
    }

    public ResponseEntity<SseEmitter> connect(String roomId) throws IOException {
        Optional<SseEmitter> sseOptional = sseRepository.find(roomId);
        if (sseOptional.isEmpty()) {
            sseRepository.create(roomId);
            sseOptional = sseRepository.find(roomId);
        }
        SseEmitter sseEmitter = sseOptional.get();

        sseEmitter.send(SseEmitter.event().
                id("C_0").
                name("[C] connect"));

        return ResponseEntity.ok(sseOptional.get());
    }

    /**
     * Waiting Room(W)
     */
    public void waitingRoomOptionChange(String roomId) throws IOException {
        Optional<SseEmitter> sseOptional = sseRepository.find(roomId);
        if (sseOptional.isEmpty()) return;

        SseEmitter sseEmitter = sseOptional.get();
        sseEmitter.send(SseEmitter
                .event()
                .id("W_0")
                .name("[W] game option is updated."));
    }

    public void waitingRoomMemberChange(String roomId, boolean join) {
        Optional<SseEmitter> sseOptional = sseRepository.find(roomId);
        if (sseOptional.isEmpty()) return;

        SseEmitter sseEmitter = sseOptional.get();

        if (join) {
            try {
                sseEmitter.send(SseEmitter
                        .event()
                        .id("W_1")
                        .name("[W] new member enter."));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                sseEmitter.send(SseEmitter
                        .event()
                        .id("W_1")
                        .name("[W] member leave game."));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void gameStart(String roomId) {
        Optional<SseEmitter> sseOptional = sseRepository.find(roomId);
        if (sseOptional.isEmpty()) return;

        SseEmitter sseEmitter = sseOptional.get();

        try {
            sseEmitter.send(SseEmitter
                    .event()
                    .id("W_2")
                    .name("[W] game is started."));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gmae (G)
     */

    public void voteSelectStart(String roomId) {
        Optional<SseEmitter> sseOptional = sseRepository.find(roomId);
        if (sseOptional.isEmpty()) return;

        SseEmitter sseEmitter = sseOptional.get();

        try {
            sseEmitter.send(SseEmitter
                    .event()
                    .id("G_1_1")
                    .name("[W] leader starts vote."));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void voteSelectEnd(String roomId) {
        Optional<SseEmitter> sseOptional = sseRepository.find(roomId);
        if (sseOptional.isEmpty()) return;

        SseEmitter sseEmitter = sseOptional.get();

        try {
            sseEmitter.send(SseEmitter
                    .event()
                    .id("G_1_2")
                    .name("[W] vote is end."));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void adventureSelectStart(String roomId) {
        Optional<SseEmitter> sseOptional = sseRepository.find(roomId);
        if (sseOptional.isEmpty()) return;

        SseEmitter sseEmitter = sseOptional.get();

        try {
            sseEmitter.send(SseEmitter
                    .event()
                    .id("G_2_1")
                    .name("[G] leader starts adventures."));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void adventureSelectEnd(String roomId) {
        Optional<SseEmitter> sseOptional = sseRepository.find(roomId);
        if (sseOptional.isEmpty()) return;

        SseEmitter sseEmitter = sseOptional.get();

        try {
            sseEmitter.send(SseEmitter
                    .event()
                    .id("G_2_2")
                    .name("[G] adventure is end."));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void voteFailFive(String roomId) {
        Optional<SseEmitter> sseOptional = sseRepository.find(roomId);
        if (sseOptional.isEmpty()) return;

        SseEmitter sseEmitter = sseOptional.get();

        try {
            sseEmitter.send(SseEmitter
                    .event()
                    .id("G_3_1")
                    .name("[G] votes fail 5th, game is end."));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void adventureTotalFail(String roomId) {
        Optional<SseEmitter> sseOptional = sseRepository.find(roomId);
        if (sseOptional.isEmpty()) return;

        SseEmitter sseEmitter = sseOptional.get();

        try {
            sseEmitter.send(SseEmitter
                    .event()
                    .id("G_3_2")
                    .name("[G] adventure can’t success 3th while 5 playing."));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void adventureTotalSuccess(String roomId) {
        Optional<SseEmitter> sseOptional = sseRepository.find(roomId);
        if (sseOptional.isEmpty()) return;

        SseEmitter sseEmitter = sseOptional.get();

        try {
            sseEmitter.send(SseEmitter
                    .event()
                    .id("G_3_3")
                    .name("[G] adventure success 3times."));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void assassinPickEnd(String roomId) {
        Optional<SseEmitter> sseOptional = sseRepository.find(roomId);
        if (sseOptional.isEmpty()) return;

        SseEmitter sseEmitter = sseOptional.get();

        try {
            sseEmitter.send(SseEmitter
                    .event()
                    .id("G_3_4")
                    .name("[G] assassin choose done."));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}
