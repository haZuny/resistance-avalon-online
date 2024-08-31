package com.rk43.avalon.sse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class SseService {

    SseRepository sseRepository;

    @Autowired
    public SseService(SseRepository emiterRepository) {
        this.sseRepository = emiterRepository;
    }

    public ResponseEntity<SseEmitter> connect(String roomId) throws IOException {
        Optional<ArrayList<SseEmitter>> sseListOptional = sseRepository.find(roomId);
        if (sseListOptional.isEmpty()) {
            sseRepository.create(roomId);
            sseListOptional = sseRepository.find(roomId);
        }
        ArrayList<SseEmitter> sseList = sseListOptional.get();

        // add new emiter
        SseEmitter newEmitter = new SseEmitter();
        sseList.add(newEmitter);

        // connect message
        newEmitter.send(SseEmitter.event().id("C_0").name("[C] connect"));

        return ResponseEntity.ok(newEmitter);
    }

    /**
     * send event
     */
    public void sendEvent(SseEmitter sse, String id, String name) throws IOException {
        sse.send(SseEmitter.event().id(id).name(name));
    }


    /**
     * Waiting Room(W)
     */
    public void waitingRoomOptionChange(String roomId) throws IOException {
        Optional<ArrayList<SseEmitter>> sseListOptional = sseRepository.find(roomId);
        if (sseListOptional.isEmpty()) return;
        ArrayList<SseEmitter> sseList = sseListOptional.get();
        for (SseEmitter sse : sseList) {
            try {
                sendEvent(sse, "W_0", "[W] game option is updated.");
            } catch (Exception e) {
            }
        }
    }

    public void waitingRoomMemberChange(String roomId, boolean join) {
        Optional<ArrayList<SseEmitter>> sseListOptional = sseRepository.find(roomId);
        if (sseListOptional.isEmpty()) return;
        ArrayList<SseEmitter> sseList = sseListOptional.get();
        for (SseEmitter sse : sseList) {
            if (join) {
                try {
                    sendEvent(sse, "W_1", "[W] new member enter.");
                } catch (Exception e) {
                }

            } else {
                try {
                    sendEvent(sse, "W_1", "[W] member leave game.");
                } catch (Exception e) {
                }

            }
        }
    }

    public void gameStart(String roomId) {
        Optional<ArrayList<SseEmitter>> sseListOptional = sseRepository.find(roomId);
        if (sseListOptional.isEmpty()) return;
        ArrayList<SseEmitter> sseList = sseListOptional.get();
        for (SseEmitter sse : sseList) {
            try {
                sendEvent(sse, "W_2", "[W] game is started.");
            } catch (Exception e) {
            }
        }
    }

    /**
     * Gmae (G)
     */

    public void voteSelectStart(String roomId) {
        Optional<ArrayList<SseEmitter>> sseListOptional = sseRepository.find(roomId);
        if (sseListOptional.isEmpty()) return;
        ArrayList<SseEmitter> sseList = sseListOptional.get();
        for (SseEmitter sse : sseList) {
            try {
                sendEvent(sse, "G_1_1", "[W] leader starts vote.");
            } catch (Exception e) {
            }
        }
    }

    public void voteSelectEnd(String roomId) {
        Optional<ArrayList<SseEmitter>> sseListOptional = sseRepository.find(roomId);
        if (sseListOptional.isEmpty()) return;
        ArrayList<SseEmitter> sseList = sseListOptional.get();
        for (SseEmitter sse : sseList) {
            try {
                sendEvent(sse, "G_1_2", "[W] vote is end.");
            } catch (Exception e) {
            }
        }
    }

    public void adventureSelectStart(String roomId) {
        Optional<ArrayList<SseEmitter>> sseListOptional = sseRepository.find(roomId);
        if (sseListOptional.isEmpty()) return;
        ArrayList<SseEmitter> sseList = sseListOptional.get();
        for (SseEmitter sse : sseList) {
            try {
                sendEvent(sse, "G_2_1", "[G] leader starts adventures.");
            } catch (Exception e) {
            }
        }
    }

    public void adventureSelectEnd(String roomId) {
        Optional<ArrayList<SseEmitter>> sseListOptional = sseRepository.find(roomId);
        if (sseListOptional.isEmpty()) return;
        ArrayList<SseEmitter> sseList = sseListOptional.get();
        for (SseEmitter sse : sseList) {
            try {
                sendEvent(sse, "G_2_2", "[G] adventure is end.");
            } catch (Exception e) {
            }
        }
    }

    public void voteFailFive(String roomId) {
        Optional<ArrayList<SseEmitter>> sseListOptional = sseRepository.find(roomId);
        if (sseListOptional.isEmpty()) return;
        ArrayList<SseEmitter> sseList = sseListOptional.get();
        for (SseEmitter sse : sseList) {
            try {
                sendEvent(sse, "G_3_1", "[G] votes fail 5th, game is end.");
            } catch (Exception e) {
            }
        }
    }

    public void adventureTotalFail(String roomId) {
        Optional<ArrayList<SseEmitter>> sseListOptional = sseRepository.find(roomId);
        if (sseListOptional.isEmpty()) return;
        ArrayList<SseEmitter> sseList = sseListOptional.get();
        for (SseEmitter sse : sseList) {
            try {
                sendEvent(sse, "G_3_2", "[G] adventure can’t success 3th while 5 playing.");
            } catch (Exception e) {
            }
        }
    }

    public void adventureTotalSuccess(String roomId) {
        Optional<ArrayList<SseEmitter>> sseListOptional = sseRepository.find(roomId);
        if (sseListOptional.isEmpty()) return;
        ArrayList<SseEmitter> sseList = sseListOptional.get();
        for (SseEmitter sse : sseList) {
            try {
                sendEvent(sse, "G_3_3", "[G] adventure success 3times.");
            } catch (Exception e) {
            }
        }
    }

    public void assassinPickEnd(String roomId) {
        Optional<ArrayList<SseEmitter>> sseListOptional = sseRepository.find(roomId);
        if (sseListOptional.isEmpty()) return;
        ArrayList<SseEmitter> sseList = sseListOptional.get();
        for (SseEmitter sse : sseList) {
            try {
                sendEvent(sse, "G_3_4", "[G] assassin choose done.");
            } catch (Exception e) {
            }
        }
    }
}
