package com.rk43.avalon.entity.game;

import com.rk43.avalon.entity.game.dto.CreateGameResponseDto;
import com.rk43.avalon.entity.game_player.GamePlayerEntity;
import com.rk43.avalon.entity.game_player.GamePlayerRepository;
import com.rk43.avalon.entity.user.UserEntity;
import com.rk43.avalon.entity.user.UserRepository;
import com.rk43.avalon.entity.waiting_room.WaitingRoomEntity;
import com.rk43.avalon.entity.waiting_room.WaitingRoomRepository;
import com.rk43.avalon.entity.waiting_room.WaitingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class GameService {
    GameRepository gameRepository;
    WaitingRoomRepository waitingRoomRepository;
    WaitingRoomService waitingRoomService;
    UserRepository userRepository;
    GamePlayerRepository gamePlayerRepository;

    @Autowired
    public GameService(GameRepository gameRepository,
                       WaitingRoomRepository waitingRoomRepository,
                       WaitingRoomService waitingRoomService,
                       UserRepository userRepository,
                       GamePlayerRepository gamePlayerRepository) {
        this.gameRepository = gameRepository;
        this.waitingRoomRepository = waitingRoomRepository;
        this.waitingRoomService = waitingRoomService;
        this.userRepository = userRepository;
        this.gamePlayerRepository = gamePlayerRepository;
    }

    // create
    public ResponseEntity<CreateGameResponseDto> createNewGame(String gameId, String userId){

        CreateGameResponseDto responseDto = new CreateGameResponseDto();
        Optional<WaitingRoomEntity> waitingRoomOptional = waitingRoomRepository.findById(gameId);
        Optional<UserEntity> userOptional = userRepository.findById(userId);

        // check if waiting room is empty
        if (waitingRoomOptional.isEmpty()){
            responseDto.setMessage(String.format("waiting room[%s] not found", gameId));
            responseDto.setStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        WaitingRoomEntity waitingRoom = waitingRoomOptional.get();

        // check user is empty
        if (userOptional.isEmpty()){
            responseDto.setMessage(String.format("user[%s] not found", userId));
            responseDto.setStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        UserEntity user = userOptional.get();

        // check user is admin
        if (waitingRoom.getAdmin().getId() != user.getId()){
            responseDto.setMessage(String.format("user[%s] is not admin", userId));
            responseDto.setStatus(HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }

        // check member cnt
        if (waitingRoom.getMember().size() +1 <= 4){
            responseDto.setMessage(String.format("member num is invalid"));
            responseDto.setStatus(HttpStatus.FORBIDDEN.value());
            return new ResponseEntity<>(responseDto, HttpStatus.FORBIDDEN);
        }

        // create game
        GameEntity game = new GameEntity();
        game.setId(waitingRoom.getId());
        game.setWaitingRoom(waitingRoom);
        ArrayList<GamePlayerEntity> gameMember = waitingRoomService.getGameMemberList(waitingRoom);
        for (GamePlayerEntity gamePlayer : gameMember){
            gamePlayerRepository.save(gamePlayer);
        }
        game.setMember(gameMember);
        game = gameRepository.save(game);

        responseDto.setData(game);
        responseDto.setMessage("new game is created.");
        responseDto.setStatus(HttpStatus.CREATED.value());
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
