package com.rk43.avalon.entity.waiting_room;

import com.rk43.avalon.entity.character.CharacterRepository;
import com.rk43.avalon.entity.user.UserEntity;
import com.rk43.avalon.entity.user.UserRepository;
import com.rk43.avalon.entity.waiting_room.dto.NicknameData;
import com.rk43.avalon.entity.waiting_room.dto.PostResponseDto;
import com.rk43.avalon.entity.waiting_room.dto.UserData;
import com.rk43.avalon.entity.waiting_room.dto.WaitingRoomData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class WaitingRoomService {
    WaitingRoomRepository waitingRoomRepository;
    UserRepository userRepository;
    CharacterRepository characterRepository;

    @Autowired
    public WaitingRoomService(WaitingRoomRepository waitingRoomRepository, UserRepository userRepository, CharacterRepository characterRepository) {
        this.waitingRoomRepository = waitingRoomRepository;
        this.userRepository = userRepository;
        this.characterRepository = characterRepository;
    }

    public ResponseEntity<PostResponseDto> createNewRoom(NicknameData nickname) {
        int lastMemberOrder = 0;
        int defaultMaximumMember = 10;

        // create new admin
        UserEntity admin = new UserEntity();
        admin.setNickname(nickname.getNickname());
        admin.setOrder(lastMemberOrder);
        admin = userRepository.save(admin);

        // create new waiting room
        WaitingRoomEntity waitingRoom = new WaitingRoomEntity();
        waitingRoom.setAdmin(admin);
        waitingRoom.setMaximumUser(defaultMaximumMember);
        waitingRoom.setLastMemberOrder(lastMemberOrder);
            // add default character
        waitingRoom.getSelectedCharacter().add(characterRepository.findById(0).get());
        waitingRoom.getSelectedCharacter().add(characterRepository.findById(1).get());
        waitingRoom.getSelectedCharacter().add(characterRepository.findById(2).get());
        waitingRoom.getSelectedCharacter().add(characterRepository.findById(4).get());
        waitingRoom.getSelectedCharacter().add(characterRepository.findById(5).get());
        waitingRoom = waitingRoomRepository.save(waitingRoom);

        // set response dto
        PostResponseDto postResponseDto = new PostResponseDto();
        postResponseDto.setData(new WaitingRoomData());
        postResponseDto.getData().setWaiting_room_id(waitingRoom.getId());
        postResponseDto.getData().setWaiting_room_admin(new UserData(admin.getId(), admin.getNickname()));
        postResponseDto.getData().setWaiting_room_member(new ArrayList<>());
        postResponseDto.getData().setWaiting_room_maximum_user(defaultMaximumMember);
        try{
            postResponseDto.getData().refreshCharacters(waitingRoom.getSelectedCharacter());
        } catch (Exception e){

        }

        postResponseDto.setStatus(HttpStatus.OK.value());
        postResponseDto.setMessage("new waiting room is created");
        return new ResponseEntity<>(postResponseDto, HttpStatus.OK);
    }
}
