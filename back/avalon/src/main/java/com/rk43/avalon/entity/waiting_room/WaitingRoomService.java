package com.rk43.avalon.entity.waiting_room;

import com.rk43.avalon.entity.DefaultResponseDto;
import com.rk43.avalon.entity.character.CharacterEntity;
import com.rk43.avalon.entity.character.CharacterRepository;
import com.rk43.avalon.entity.game_player.GamePlayerEntity;
import com.rk43.avalon.entity.user.UserEntity;
import com.rk43.avalon.entity.user.UserRepository;
import com.rk43.avalon.entity.waiting_room.dto.*;
import com.rk43.avalon.sse.SseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@Service
public class WaitingRoomService {
    WaitingRoomRepository waitingRoomRepository;
    UserRepository userRepository;
    CharacterRepository characterRepository;
    SseService sseService;

    @Autowired
    public WaitingRoomService(
            WaitingRoomRepository waitingRoomRepository,
            UserRepository userRepository,
            CharacterRepository characterRepository,
            SseService sseService) {
        this.waitingRoomRepository = waitingRoomRepository;
        this.userRepository = userRepository;
        this.characterRepository = characterRepository;
        this.sseService = sseService;
    }

    public ResponseEntity<CreateRoomResponseDto> createNewRoom(NicknameData nickname) {
        int defaultMaximumMember = 10;

        // create new admin
        UserEntity admin = new UserEntity();
        admin.setNickname(nickname.getNickname());
        admin = userRepository.save(admin);

        // create new waiting room
        WaitingRoomEntity waitingRoom = new WaitingRoomEntity();
        waitingRoom.setAdmin(admin);
        waitingRoom.setMaximumUser(defaultMaximumMember);
        waitingRoom = waitingRoomRepository.save(waitingRoom);

        // set response dto
        CreateRoomResponseDto postResponseDto = new CreateRoomResponseDto();
        postResponseDto.setData(waitingRoom.getId(), admin);

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
        } catch (Exception e){}

        responseDto.setMessage("success");
        responseDto.setStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<DefaultResponseDto> updateWaitingRoom(String waitingRoomId, String userId, UpdateWaitingRoomRequestDto requestDto){

        DefaultResponseDto responseDto = new DefaultResponseDto();

        Optional<WaitingRoomEntity> waitingRoomOptional = waitingRoomRepository.findById(waitingRoomId);

        // check waiting room not found
        if (waitingRoomOptional.isEmpty())  {
            responseDto.setMessage(String.format("waiting room[%s] not found", waitingRoomId));
            responseDto.setStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }

        WaitingRoomEntity waitingRoom = waitingRoomOptional.get();

        // check user is admin
        if (!waitingRoom.getAdmin().getId().equals(userId)){
            responseDto.setMessage(String.format("user[%s] is not admin", userId));
            responseDto.setStatus(HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }

        // set maximum user
        if(requestDto.getMaximum_user() != -1){
            int maximum = requestDto.getMaximum_user();
            // check maximum is valid
            if (maximum <= 4 || maximum >= 11 || maximum <= waitingRoom.getMember().size()){
                responseDto.setMessage(String.format("maximum value is invalid"));
                responseDto.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
            }
            waitingRoom.setMaximumUser(maximum);
            waitingRoomRepository.save(waitingRoom);
        }

        // set selected character
        if (requestDto.getSelected_character() != null){
            boolean selectedMordred = false;
            boolean selectedOberon = false;

            for (String character : requestDto.getSelected_character()){
                if (!selectedMordred && character.equals("mordred"))
                    selectedMordred = true;
                else if(!selectedOberon && character.equals("oberon"))
                    selectedOberon = true;
                else{
                    responseDto.setMessage(String.format("selected values are invalid"));
                    responseDto.setStatus(HttpStatus.BAD_REQUEST.value());
                    return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
                }
            }

            // check member cnt valid
            if ((waitingRoom.getMember().size() + 1 <= 6 && (selectedMordred || selectedOberon)) ||
                    (waitingRoom.getMember().size() + 1 <= 9 && (selectedMordred && selectedOberon))){
                responseDto.setMessage(String.format("selected values are invalid"));
                responseDto.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
            }

            ArrayList<CharacterEntity> temp = new ArrayList<>();

            if (selectedMordred)
                temp.add(characterRepository.findById(6).get());
            if (selectedOberon)
                temp.add(characterRepository.findById(7).get());

            waitingRoom.setSelectedCharacter(temp);
        }

        // sse send
        try {
            sseService.waitingRoomOptionChange(waitingRoomId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // response
        responseDto.setMessage(String.format("game option is successfully updated."));
        responseDto.setStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<UpdateMemberResponseDto> updateMember(String waitingRoomId, String userId, boolean join, NicknameData nicknameData){

        UpdateMemberResponseDto responseDto = new UpdateMemberResponseDto();
        Optional<WaitingRoomEntity> waitingRoomOptional = waitingRoomRepository.findById(waitingRoomId);

        // check waiting room not found
        if (waitingRoomOptional.isEmpty())  {
            responseDto.setMessage(String.format("waiting room[%s] not found", waitingRoomId));
            responseDto.setStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }

        WaitingRoomEntity waitingRoom = waitingRoomOptional.get();

        // join
        if (join){
            // if user already join
            if (waitingRoom.containsUser(userId)){
                responseDto.setMessage("user is already member.");
                responseDto.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
            }

            // if maximum < cnt
            if (waitingRoom.getMember().size() + 1 == waitingRoom.getMaximumUser()){
                responseDto.setMessage("member count is maximum.");
                responseDto.setStatus(HttpStatus.FORBIDDEN.value());
                return new ResponseEntity<>(responseDto, HttpStatus.FORBIDDEN);
            }

            // if nickname duplicate
            if (!nicknameData.getNickname().isEmpty()){
                boolean flag = false;
                if (waitingRoom.getAdmin().getNickname().equals(nicknameData.getNickname()))
                    flag = true;
                for (UserEntity user : waitingRoom.getMember()){
                    if (user.getNickname().equals(nicknameData.getNickname()))
                        flag = true;
                }
                if (flag){
                    responseDto.setMessage("nickname duplicate.");
                    responseDto.setStatus(HttpStatus.FORBIDDEN.value());
                    return new ResponseEntity<>(responseDto, HttpStatus.FORBIDDEN);
                }
            }

            // create new User
            UserEntity user = new UserEntity();
            user.setNickname(nicknameData.getNickname());
            user = userRepository.save(user);
            waitingRoom.getMember().add(user);

            // set response dto
            responseDto.setData(waitingRoom.getId(), user);
        }

        // withdraw
        else{
            // user-id is null
            if (userId == null){
                responseDto.setMessage("user-id is null.");
                responseDto.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
            }

            // if user isn't member
            if (!waitingRoom.containsUser(userId)){
                responseDto.setMessage("user is not member.");
                responseDto.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
            }

            // if last user
            if (waitingRoom.getMember().isEmpty()){
                waitingRoomRepository.deleteById(waitingRoom.getId());
                userRepository.deleteById(userId);
                responseDto.setStatus(HttpStatus.OK.value());
                responseDto.setMessage("member is successfully updated");
                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            }

            // if admin
            if (waitingRoom.getAdmin().getId().equals(userId)){
                UserEntity nextAdmin = waitingRoom.getMember().getFirst();
                waitingRoom.setAdmin(nextAdmin);
                waitingRoom.getMember().removeIf(user -> user.getId().equals(nextAdmin.getId()));
            }
            // if user
            else{
                waitingRoom.getMember().removeIf(user -> user.getId().equals(userId));
            }

            waitingRoom.refreshCharacters();
        }

        sseService.waitingRoomMemberChange(waitingRoomId, join);

        responseDto.setStatus(HttpStatus.OK.value());
        responseDto.setMessage("member is successfully updated");
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // mapping character per member
    public ArrayList<GamePlayerEntity> getGameMemberList(WaitingRoomEntity waitingRoom){
        // shuffled user list
        ArrayList<UserEntity> shuffleUsers = new ArrayList<>(waitingRoom.getMember());
        shuffleUsers.add(waitingRoom.getAdmin());
        Collections.shuffle(shuffleUsers);

        // selected character count(evil cnt)
        int selectedCharacterCnt = waitingRoom.getSelectedCharacter().size();

        // character list
        ArrayList<CharacterEntity> characterList = new ArrayList<>(Arrays.asList(
                // good
                characterRepository.findById(0).get(),
                characterRepository.findById(1).get(),
                characterRepository.findById(2).get(),
                // evil
                characterRepository.findById(4).get(),
                characterRepository.findById(5).get()
        ));
        for (CharacterEntity selected : waitingRoom.getSelectedCharacter()){
            characterList.add(selected);
        }

        // add default character by member num
        if (shuffleUsers.size() == 5){} // 3:2
        else if (shuffleUsers.size() == 6){ // 4:2
            // good
            characterList.add(characterRepository.findById(0).get());
        }
        else if (shuffleUsers.size() == 7){ // 4:3
            // good
            characterList.add(characterRepository.findById(0).get());
            // evil
            if (selectedCharacterCnt < 1)
                characterList.add(characterRepository.findById(3).get());
        }
        else if (shuffleUsers.size() == 8){ // 5:3
            // good
            characterList.add(characterRepository.findById(0).get());
            characterList.add(characterRepository.findById(0).get());
            // evil
            if (selectedCharacterCnt < 1)
                characterList.add(characterRepository.findById(3).get());
        }
        else if (shuffleUsers.size() == 9){ // 6:3
            // good
            characterList.add(characterRepository.findById(0).get());
            characterList.add(characterRepository.findById(0).get());
            characterList.add(characterRepository.findById(0).get());
            // evil
            if (selectedCharacterCnt < 1)
                characterList.add(characterRepository.findById(3).get());
        }
        else if (shuffleUsers.size() == 10){    // 6:4
            // good
            characterList.add(characterRepository.findById(0).get());
            characterList.add(characterRepository.findById(0).get());
            characterList.add(characterRepository.findById(0).get());
            // evil
            if (selectedCharacterCnt == 0){
                characterList.add(characterRepository.findById(3).get());
                characterList.add(characterRepository.findById(3).get());
            }
            else if (selectedCharacterCnt == 1)
                characterList.add(characterRepository.findById(3).get());
        }

        // mapping user and character
        ArrayList<GamePlayerEntity> mappedList = new ArrayList<>();
        for (int i = 0; i < shuffleUsers.size(); i++){
            GamePlayerEntity gamePlayer = new GamePlayerEntity();
            gamePlayer.setId(shuffleUsers.get(i).getId());
            gamePlayer.setUser(shuffleUsers.get(i));
            gamePlayer.setCharacter(characterList.get(i));
            mappedList.add(gamePlayer);
        }

        return mappedList;
    }
}
