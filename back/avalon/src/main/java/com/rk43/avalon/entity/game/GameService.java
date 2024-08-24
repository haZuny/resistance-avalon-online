package com.rk43.avalon.entity.game;

import com.rk43.avalon.entity.DefaultResponseDto;
import com.rk43.avalon.entity.game.dto.CreateGameResponseDto;
import com.rk43.avalon.entity.game.dto.CreateNewVoteResponseDto;
import com.rk43.avalon.entity.game.dto.GetVotesResponseDto;
import com.rk43.avalon.entity.game.dto.SelectRequestDto;
import com.rk43.avalon.entity.game_player.GamePlayerEntity;
import com.rk43.avalon.entity.game_player.GamePlayerRepository;
import com.rk43.avalon.entity.select.SelectEntity;
import com.rk43.avalon.entity.select.SelectRepository;
import com.rk43.avalon.entity.user.UserEntity;
import com.rk43.avalon.entity.user.UserRepository;
import com.rk43.avalon.entity.vote.VoteEntity;
import com.rk43.avalon.entity.vote.VoteRepository;
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
    VoteRepository voteRepository;
    SelectRepository selectRepository;

    @Autowired
    public GameService(GameRepository gameRepository,
                       WaitingRoomRepository waitingRoomRepository,
                       WaitingRoomService waitingRoomService,
                       UserRepository userRepository,
                       GamePlayerRepository gamePlayerRepository,
                       VoteRepository voteRepository,
                       SelectRepository selectRepository) {
        this.gameRepository = gameRepository;
        this.waitingRoomRepository = waitingRoomRepository;
        this.waitingRoomService = waitingRoomService;
        this.userRepository = userRepository;
        this.gamePlayerRepository = gamePlayerRepository;
        this.voteRepository = voteRepository;
        this.selectRepository = selectRepository;
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


    public ResponseEntity<CreateNewVoteResponseDto> createNewVote(String gameId, String userId){

        CreateNewVoteResponseDto responseDto = new CreateNewVoteResponseDto();

        // fine game
        Optional<GameEntity> gameOptional = gameRepository.findById(gameId);
        if (gameOptional.isEmpty()){
            responseDto.setMessage(String.format("game[%s] not found", gameId));
            responseDto.setStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        GameEntity game = gameOptional.get();

        // check user is leader
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty() || !userId.equals(game.getMember().get(game.getLeader_idx()).getId())){
            responseDto.setMessage(String.format("player[%s] is not leader", userId));
            responseDto.setStatus(HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }

        // create new vote
        VoteEntity vote = new VoteEntity();
        vote.setSelects(new ArrayList<>());
        vote = voteRepository.save(vote);
        game.getVotes().add(vote);

        // response
        responseDto.setData(vote.getId());
        responseDto.setMessage("new vote is created");
        responseDto.setStatus(HttpStatus.CREATED.value());
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    public ResponseEntity<GetVotesResponseDto> getVotes(String gameId, String userId){

        GetVotesResponseDto responseDto = new GetVotesResponseDto();

        // check game is empty
        Optional<GameEntity> gameOptional = gameRepository.findById(gameId);
        if (gameOptional.isEmpty()){
            responseDto.setMessage(String.format("game[%s] not found", gameId));
            responseDto.setStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        GameEntity game = gameOptional.get();

        // check user auth
        Optional<GamePlayerEntity> gamePlayerOptional = gamePlayerRepository.findById(userId);
        if (gamePlayerOptional.isEmpty() || game.getWaitingRoom().containsUser(userId)){
            responseDto.setMessage(String.format("user[%s] is not member", userId));
            responseDto.setStatus(HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }

        // check vote is empty
        if (game.getVotes().isEmpty()){
            responseDto.setMessage(String.format("request success"));
            responseDto.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        VoteEntity lastVote = game.getVotes().getLast();
        responseDto.setData(lastVote);
        responseDto.setMessage(String.format("request success"));
        responseDto.setStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<DefaultResponseDto> addVoteSelect(String gameId, String userId, SelectRequestDto voted){

        DefaultResponseDto responseDto = new DefaultResponseDto();

        // check game is empty
        Optional<GameEntity> gameOptional = gameRepository.findById(gameId);
        if (gameOptional.isEmpty()){
            responseDto.setMessage(String.format("game[%s] not found", gameId));
            responseDto.setStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        GameEntity game = gameOptional.get();

        // check user auth
        Optional<GamePlayerEntity> gamePlayerOptional = gamePlayerRepository.findById(userId);
        if (gamePlayerOptional.isEmpty() || !game.getWaitingRoom().containsUser(userId)){
            responseDto.setMessage(String.format("user[%s] is not member", userId));
            responseDto.setStatus(HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }
        GamePlayerEntity gamePlayer = gamePlayerOptional.get();

        // get last vote
        if (game.getVotes().isEmpty()){
            responseDto.setMessage(String.format("vote is empty"));
            responseDto.setStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        VoteEntity lastVote = game.getVotes().getLast();

        // check if vote end
        if (lastVote.isEnd(game)){
            responseDto.setMessage(String.format("vote is already end"));
            responseDto.setStatus(HttpStatus.FORBIDDEN.value());
            return new ResponseEntity<>(responseDto, HttpStatus.FORBIDDEN);
        }

        // check if user already voted
        if (lastVote.userVoted(gamePlayer)){
            responseDto.setMessage(String.format("user already voted"));
            responseDto.setStatus(HttpStatus.FORBIDDEN.value());
            return new ResponseEntity<>(responseDto, HttpStatus.FORBIDDEN);
        }

        // create select
        SelectEntity select = new SelectEntity();
        select.setVoted(voted.isVoted());
        select.setGamePlayer(gamePlayer);
        select = selectRepository.save(select);
        lastVote.getSelects().add(select);

        // check vote is end
        if (lastVote.isEnd(game)){
            lastVote.setResult();
            game.nextTurn(lastVote);
        }

        // return
        responseDto.setMessage(String.format("select is appended to vote"));
        responseDto.setStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
