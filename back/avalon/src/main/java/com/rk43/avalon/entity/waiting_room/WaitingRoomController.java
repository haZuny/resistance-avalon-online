package com.rk43.avalon.entity.waiting_room;

import com.rk43.avalon.entity.waiting_room.dto.NicknameData;
import com.rk43.avalon.entity.waiting_room.dto.PostResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/waiting-room")
public class WaitingRoomController {
    WaitingRoomService waitingRoomService;

    @Autowired
    public WaitingRoomController(WaitingRoomService waitingRoomService) {
        this.waitingRoomService = waitingRoomService;
    }

    // create new waitingRoom
    @PostMapping("")
    public ResponseEntity<PostResponseDto> post(@RequestBody NicknameData nickname){
        return waitingRoomService.createNewRoom(nickname);
    }


}
