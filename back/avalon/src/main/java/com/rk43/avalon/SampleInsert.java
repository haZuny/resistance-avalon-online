package com.rk43.avalon;

import com.rk43.avalon.entity.game.GameService;
import com.rk43.avalon.entity.waiting_room.WaitingRoomService;
import com.rk43.avalon.entity.waiting_room.dto.CreateRoomResponseDto;
import com.rk43.avalon.entity.waiting_room.dto.NicknameData;
import com.rk43.avalon.entity.waiting_room.dto.UpdateMemberResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SampleInsert {
    GameService gameService;
    WaitingRoomService waitingRoomService;

    @Autowired
    public SampleInsert(GameService gameService, WaitingRoomService waitingRoomService) {
        this.gameService = gameService;
        this.waitingRoomService = waitingRoomService;

//        // create waitingroom
//        CreateRoomResponseDto createRoomResponseDto = waitingRoomService.createNewRoom(new NicknameData()).getBody();
//        String waitingRoomId = createRoomResponseDto.getData().getWaiting_room_id();
//        String adminId = createRoomResponseDto.getData().getWaiting_room_admin().getId();
//        System.out.println(String.format("waiting room is created :: waiting_room_id[%s], admin_id[%s]", waitingRoomId, adminId));
//
//        // member join
//        UpdateMemberResponseDto updateMemberResponseDto1 = waitingRoomService.updateMember(waitingRoomId, "", true, new NicknameData()).getBody();
//        String member1Id = updateMemberResponseDto1.getData().getWaiting_room_created_user().getId();
//        System.out.println(String.format("new user is join :: waiting_room_id[%s], user_id[%s]", waitingRoomId, member1Id));
//
//        UpdateMemberResponseDto updateMemberResponseDto2 = waitingRoomService.updateMember(waitingRoomId, "", true, new NicknameData()).getBody();
//        String member2Id = updateMemberResponseDto2.getData().getWaiting_room_created_user().getId();
//        System.out.println(String.format("new user is join :: waiting_room_id[%s], user_id[%s]", waitingRoomId, member2Id));
//
//        UpdateMemberResponseDto updateMemberResponseDto3 = waitingRoomService.updateMember(waitingRoomId, "", true, new NicknameData()).getBody();
//        String member3Id = updateMemberResponseDto3.getData().getWaiting_room_created_user().getId();
//        System.out.println(String.format("new user is join :: waiting_room_id[%s], user_id[%s]", waitingRoomId, member3Id));
//
//        UpdateMemberResponseDto updateMemberResponseDto4 = waitingRoomService.updateMember(waitingRoomId, "", true, new NicknameData()).getBody();
//        String member4Id = updateMemberResponseDto4.getData().getWaiting_room_created_user().getId();
//        System.out.println(String.format("new user is join :: waiting_room_id[%s], user_id[%s]", waitingRoomId, member4Id));
//
//        // start game
//        gameService.createNewVote(waitingRoomId, adminId);



    }
}
