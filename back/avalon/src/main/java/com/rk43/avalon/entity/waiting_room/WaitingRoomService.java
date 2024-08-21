package com.rk43.avalon.entity.waiting_room;

import com.rk43.avalon.entity.DefaultResponseDto;
import com.rk43.avalon.entity.character.CharacterRepository;
import com.rk43.avalon.entity.user.UserEntity;
import com.rk43.avalon.entity.user.UserRepository;
import com.rk43.avalon.entity.waiting_room.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

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

    public ResponseEntity<GetIdResponseDto> getWaitingRoom(String waitingRoomId, String userId){

        GetIdResponseDto getIdResponseDto = new GetIdResponseDto();
        Optional<WaitingRoomEntity> waitingRoomOptional = waitingRoomRepository.findById(waitingRoomId);

        // check waiting room not found
        if (waitingRoomOptional.isEmpty())  {
            getIdResponseDto.setMessage(String.format("waiting room[%s] not found", waitingRoomId));
            getIdResponseDto.setStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(getIdResponseDto, HttpStatus.NOT_FOUND);
        }

        WaitingRoomEntity waitingRoom = waitingRoomOptional.get();

        // check user contained
        if (!waitingRoom.containsUser(userId)){
            getIdResponseDto.setMessage(String.format("user[%s] has no access", userId));
            getIdResponseDto.setStatus(HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(getIdResponseDto, HttpStatus.UNAUTHORIZED);
        }

        // set response dto
        GetIdResponseDto responseDto = new GetIdResponseDto();
        responseDto.setData(new WaitingRoomData());
        responseDto.getData().setWaiting_room_id(waitingRoom.getId());
        responseDto.getData().setWaiting_room_admin(new UserData(waitingRoom.getAdmin().getId(), waitingRoom.getAdmin().getNickname()));
        responseDto.getData().setWaiting_room_member(new ArrayList<>());
        for (UserEntity user : waitingRoom.getMember()){
            responseDto.getData().getWaiting_room_member().add(new UserData(user.getId(), user.getNickname()));
        }
        responseDto.getData().setWaiting_room_maximum_user(waitingRoom.getMaximumUser());
        try{
            responseDto.getData().refreshCharacters(waitingRoom.getSelectedCharacter());
        } catch (Exception e){

        }

        responseDto.setMessage("success");
        responseDto.setStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
