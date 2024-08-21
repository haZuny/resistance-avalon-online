package com.rk43.avalon.entity.waiting_room;

import com.rk43.avalon.entity.waiting_room.dto.GetIdResponseDto;
import com.rk43.avalon.entity.waiting_room.dto.NicknameData;
import com.rk43.avalon.entity.waiting_room.dto.PostResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // request waitingRoom
    @GetMapping("/{waitingRoomId}")
    public ResponseEntity<GetIdResponseDto> get(@PathVariable String waitingRoomId, @RequestParam(name = "user-id") String userId){
        return waitingRoomService.getWaitingRoom(waitingRoomId, userId);
    }


}
