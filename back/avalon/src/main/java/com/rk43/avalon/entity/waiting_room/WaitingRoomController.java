package com.rk43.avalon.entity.waiting_room;

import com.rk43.avalon.entity.DefaultResponseDto;
import com.rk43.avalon.entity.waiting_room.dto.*;
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
    public ResponseEntity<CreateRoomResponseDto> createWaitingRoom(@RequestBody NicknameData nickname){
        return waitingRoomService.createNewRoom(nickname);
    }

    // request waitingRoom
    @GetMapping("/{waitingRoomId}")
    public ResponseEntity<GetIdResponseDto> getWaitingRoom(@PathVariable String waitingRoomId, @RequestParam(name = "user-id") String userId){
        return waitingRoomService.getWaitingRoom(waitingRoomId, userId);
    }

    // update room
    @PatchMapping("/{waitingRoomId}")
    public ResponseEntity<DefaultResponseDto> updateWaitingRoom(@PathVariable String waitingRoomId,
                                                                @RequestParam(name = "user-id") String userId,
                                                                @RequestBody UpdateWaitingRoomRequestDto requestDto){
        return waitingRoomService.updateWaitingRoom(waitingRoomId, userId, requestDto);
    }


    // join
    @PostMapping("/{waitingRoomId}/member")
    public ResponseEntity<UpdateMemberResponseDto> updateMember(
            @PathVariable String waitingRoomId
            , @RequestParam(name = "user-id", required = false) String userId
            , @RequestParam boolean join,
            @RequestBody NicknameData nickname){
        return waitingRoomService.updateMember(waitingRoomId, userId, join, nickname);
    }


}
