package com.rk43.avalon.entity.game;

import com.rk43.avalon.entity.DefaultResponseDto;
import com.rk43.avalon.entity.adventure.AdventureEntity;
import com.rk43.avalon.entity.adventure.AdventureRepository;
import com.rk43.avalon.entity.game.dto.*;
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
import com.rk43.avalon.sse.SseService;
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
    AdventureRepository adventureRepository;
    SelectRepository selectRepository;
    SseService sseService;

    @Autowired
    public GameService(GameRepository gameRepository,
                       WaitingRoomRepository waitingRoomRepository,
                       WaitingRoomService waitingRoomService,
                       UserRepository userRepository,
                       GamePlayerRepository gamePlayerRepository,
                       VoteRepository voteRepository,
                       AdventureRepository adventureRepository,
                       SelectRepository selectRepository,
                       SseService sseService) {
        this.gameRepository = gameRepository;
        this.waitingRoomRepository = waitingRoomRepository;
        this.waitingRoomService = waitingRoomService;
        this.userRepository = userRepository;
        this.gamePlayerRepository = gamePlayerRepository;
        this.voteRepository = voteRepository;
        this.adventureRepository = adventureRepository;
        this.selectRepository = selectRepository;
        this.sseService = sseService;
    }

    // create
    public ResponseEntity<CreateGameResponseDto> createNewGame(String gameId, String userId) {

        CreateGameResponseDto responseDto = new CreateGameResponseDto();
        Optional<WaitingRoomEntity> waitingRoomOptional = waitingRoomRepository.findById(gameId);
        Optional<UserEntity> userOptional = userRepository.findById(userId);

        // check if waiting room is empty
        if (waitingRoomOptional.isEmpty()) {
            responseDto.setMessage(String.format("waiting room[%s] not found", gameId));
            responseDto.setStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        WaitingRoomEntity waitingRoom = waitingRoomOptional.get();

        // check user is empty
        if (userOptional.isEmpty()) {
            responseDto.setMessage(String.format("user[%s] not found", userId));
            responseDto.setStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        UserEntity user = userOptional.get();

        // check user is admin
        if (waitingRoom.getAdmin().getId() != user.getId()) {
            responseDto.setMessage(String.format("user[%s] is not admin", userId));
            responseDto.setStatus(HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }

        // check member cnt
        if (waitingRoom.getMember().size() + 1 <= 4) {
            responseDto.setMessage(String.format("member num is invalid"));
            responseDto.setStatus(HttpStatus.FORBIDDEN.value());
            return new ResponseEntity<>(responseDto, HttpStatus.FORBIDDEN);
        }

        // create game
        GameEntity game = new GameEntity();
        game.setId(waitingRoom.getId());
        game.setWaitingRoom(waitingRoom);
        ArrayList<GamePlayerEntity> gameMember = waitingRoomService.getGameMemberList(waitingRoom);
        for (GamePlayerEntity gamePlayer : gameMember) {
            gamePlayerRepository.save(gamePlayer);
        }
        game.setMember(gameMember);
        game = gameRepository.save(game);

        // sse
        sseService.gameStart(gameId);


        responseDto.setData(game);
        responseDto.setMessage("new game is created.");
        responseDto.setStatus(HttpStatus.CREATED.value());
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    public ResponseEntity<GetGameResponseDto> getGame(String gameId, String userId) {

        GetGameResponseDto responseDto = new GetGameResponseDto();

        // check game is empty
        Optional<GameEntity> gameOptional = gameRepository.findById(gameId);
        if (gameOptional.isEmpty()) {
            responseDto.setMessage(String.format("game[%s] not found", gameId));
            responseDto.setStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        GameEntity game = gameOptional.get();

        // check user auth
        Optional<GamePlayerEntity> gamePlayerOptional = gamePlayerRepository.findById(userId);
        if (gamePlayerOptional.isEmpty() || !game.getWaitingRoom().containsUser(userId)) {
            responseDto.setMessage(String.format("user[%s] is not member", userId));
            responseDto.setStatus(HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }

        responseDto.setData(game);
        responseDto.setMessage(String.format("request success"));
        responseDto.setStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    public ResponseEntity<CreateNewVoteResponseDto> createNewVote(String gameId, String userId) {

        CreateNewVoteResponseDto responseDto = new CreateNewVoteResponseDto();

        // fine game
        Optional<GameEntity> gameOptional = gameRepository.findById(gameId);
        if (gameOptional.isEmpty()) {
            responseDto.setMessage(String.format("game[%s] not found", gameId));
            responseDto.setStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        GameEntity game = gameOptional.get();

        // check user is leader
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty() || !userId.equals(game.getMember().get(game.getLeader_idx()).getId())) {
            responseDto.setMessage(String.format("player[%s] is not leader", userId));
            responseDto.setStatus(HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }

        // create new vote
        VoteEntity vote = new VoteEntity();
        vote.setSelects(new ArrayList<>());
        vote = voteRepository.save(vote);
        game.getVotes().add(vote);

        // sse
        sseService.voteSelectStart(gameId);

        // response
        responseDto.setData(vote.getId());
        responseDto.setMessage("new vote is created");
        responseDto.setStatus(HttpStatus.CREATED.value());
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    public ResponseEntity<GetVoteResponseDto> getVotes(String gameId, String userId) {

        GetVoteResponseDto responseDto = new GetVoteResponseDto();

        // check game is empty
        Optional<GameEntity> gameOptional = gameRepository.findById(gameId);
        if (gameOptional.isEmpty()) {
            responseDto.setMessage(String.format("game[%s] not found", gameId));
            responseDto.setStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        GameEntity game = gameOptional.get();

        // check user auth
        Optional<GamePlayerEntity> gamePlayerOptional = gamePlayerRepository.findById(userId);
        if (gamePlayerOptional.isEmpty() || !game.getWaitingRoom().containsUser(userId)) {
            responseDto.setMessage(String.format("user[%s] is not member", userId));
            responseDto.setStatus(HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }

        // check vote is empty
        if (game.getVotes().isEmpty()) {
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

    public ResponseEntity<GetVoteResponseDto> getVote(int voteId, String userId) {

        GetVoteResponseDto responseDto = new GetVoteResponseDto();

        // get vote
        Optional<VoteEntity> voteOptional = voteRepository.findById(voteId);
        if (voteOptional.isEmpty()) {
            responseDto.setMessage(String.format("vote[%s] not found", voteId));
            responseDto.setStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        VoteEntity vote = voteOptional.get();

        // check user auth
//        Optional<GamePlayerEntity> gamePlayerOptional = gamePlayerRepository.findById(userId);
//        if (gamePlayerOptional.isEmpty() || game.getWaitingRoom().containsUser(userId)){
//            responseDto.setMessage(String.format("user[%s] is not member", userId));
//            responseDto.setStatus(HttpStatus.UNAUTHORIZED.value());
//            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
//        }

        // check vote is empty
//        if (game.getVotes().isEmpty()){
//            responseDto.setMessage(String.format("request success"));
//            responseDto.setStatus(HttpStatus.OK.value());
//            return new ResponseEntity<>(responseDto, HttpStatus.OK);
//        }

        responseDto.setData(vote);
        responseDto.setMessage(String.format("request success"));
        responseDto.setStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<DefaultResponseDto> addVoteSelect(String gameId, String userId, SelectRequestDto voted) {

        DefaultResponseDto responseDto = new DefaultResponseDto();

        // check game is empty
        Optional<GameEntity> gameOptional = gameRepository.findById(gameId);
        if (gameOptional.isEmpty()) {
            responseDto.setMessage(String.format("game[%s] not found", gameId));
            responseDto.setStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        GameEntity game = gameOptional.get();

        // check user auth
        Optional<GamePlayerEntity> gamePlayerOptional = gamePlayerRepository.findById(userId);
        if (gamePlayerOptional.isEmpty() || !game.getWaitingRoom().containsUser(userId)) {
            responseDto.setMessage(String.format("user[%s] is not member", userId));
            responseDto.setStatus(HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }
        GamePlayerEntity gamePlayer = gamePlayerOptional.get();

        // get last vote
        if (game.getVotes().isEmpty()) {
            responseDto.setMessage(String.format("vote is empty"));
            responseDto.setStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        VoteEntity lastVote = game.getVotes().getLast();

        // check if vote end
        if (lastVote.isEnd(game)) {
            responseDto.setMessage(String.format("vote is already end"));
            responseDto.setStatus(HttpStatus.FORBIDDEN.value());
            return new ResponseEntity<>(responseDto, HttpStatus.FORBIDDEN);
        }

        // check if user already voted
        if (lastVote.userVoted(gamePlayer)) {
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
        if (lastVote.isEnd(game)) {
            lastVote.setResult();
            voteEnd(game, lastVote);
            sseService.voteSelectEnd(gameId);
        }

        // return
        responseDto.setMessage(String.format("select is appended to vote"));
        responseDto.setStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    public ResponseEntity<CreateAdventureResponseDto> createAdventure(String gameId, String userId, AdventureMember member) {

        CreateAdventureResponseDto responseDto = new CreateAdventureResponseDto();

        // fine game
        Optional<GameEntity> gameOptional = gameRepository.findById(gameId);
        if (gameOptional.isEmpty()) {
            responseDto.setMessage(String.format("game[%s] not found", gameId));
            responseDto.setStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        GameEntity game = gameOptional.get();

        // check user is leader
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty() || !userId.equals(game.getMember().get(game.getLeader_idx()).getId())) {
            responseDto.setMessage(String.format("player[%s] is not leader", userId));
            responseDto.setStatus(HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }
        UserEntity user = userOptional.get();

        // check request body :: empty or not game member, contain leader
        // create member list
        ArrayList<GamePlayerEntity> gameMember = new ArrayList<>();
        boolean paramInvalid = member.getMember().isEmpty();
        for (String memberId : member.getMember()) {
            if (!game.getWaitingRoom().containsUser(memberId)) {
                paramInvalid = true;
                break;
            }
            gameMember.add(gamePlayerRepository.findById(memberId).get());
        }
        if (!member.getMember().contains(game.getMember().get(game.getLeader_idx()).getId())) paramInvalid = true;
        if (paramInvalid) {
            responseDto.setMessage(String.format("member value is invalid"));
            responseDto.setStatus(HttpStatus.FORBIDDEN.value());
            return new ResponseEntity<>(responseDto, HttpStatus.FORBIDDEN);
        }


        // check last vote
        // pass;;;;

        // create new adventure
        AdventureEntity adventure = new AdventureEntity();
        adventure.setSelects(new ArrayList<>());
        adventure.setMember(gameMember);
        adventure = adventureRepository.save(adventure);
        game.getAdventures().add(adventure);

        // sse
        sseService.adventureSelectStart(gameId);

        // response
        responseDto.setData(adventure);
        responseDto.setMessage("new adventure is created");
        responseDto.setStatus(HttpStatus.CREATED.value());
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    public ResponseEntity<GetAdventureResponseDto> getAdventure(String gameId, String userId) {

        GetAdventureResponseDto responseDto = new GetAdventureResponseDto();

        // check game is empty
        Optional<GameEntity> gameOptional = gameRepository.findById(gameId);
        if (gameOptional.isEmpty()) {
            responseDto.setMessage(String.format("game[%s] not found", gameId));
            responseDto.setStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        GameEntity game = gameOptional.get();

        // check user auth
        Optional<GamePlayerEntity> gamePlayerOptional = gamePlayerRepository.findById(userId);
        if (gamePlayerOptional.isEmpty() || !game.getWaitingRoom().containsUser(userId)) {
            responseDto.setMessage(String.format("user[%s] is not member", userId));
            responseDto.setStatus(HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }

        // check adventure is empty
        if (game.getAdventures().isEmpty()) {
            responseDto.setMessage(String.format("request success"));
            responseDto.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        AdventureEntity lastAdventure = game.getAdventures().getLast();
        responseDto.setData(lastAdventure);
        responseDto.setMessage(String.format("request success"));
        responseDto.setStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<DefaultResponseDto> addAdventueSelect(String gameId, String userId, SelectRequestDto voted) {

        DefaultResponseDto responseDto = new DefaultResponseDto();

        // check game is empty
        Optional<GameEntity> gameOptional = gameRepository.findById(gameId);
        if (gameOptional.isEmpty()) {
            responseDto.setMessage(String.format("game[%s] not found", gameId));
            responseDto.setStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        GameEntity game = gameOptional.get();

        // get last vote
        if (game.getAdventures().isEmpty()) {
            responseDto.setMessage(String.format("adventure is empty"));
            responseDto.setStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        AdventureEntity lastAdventure = game.getAdventures().getLast();

        // check user auth
        Optional<GamePlayerEntity> gamePlayerOptional = gamePlayerRepository.findById(userId);
        boolean userIsMember = false;
        for (GamePlayerEntity gamePlayer : lastAdventure.getMember()) {
            if (userId.equals(gamePlayer.getId())) {
                userIsMember = true;
                break;
            }
        }
        if (gamePlayerOptional.isEmpty() || !userIsMember) {
            responseDto.setMessage(String.format("user[%s] is not member", userId));
            responseDto.setStatus(HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }
        GamePlayerEntity gamePlayer = gamePlayerOptional.get();


        // check if vote end
        if (lastAdventure.isEnd()) {
            responseDto.setMessage(String.format("vote is already end"));
            responseDto.setStatus(HttpStatus.FORBIDDEN.value());
            return new ResponseEntity<>(responseDto, HttpStatus.FORBIDDEN);
        }

        // check if user already voted
        if (lastAdventure.userVoted(gamePlayer)) {
            responseDto.setMessage(String.format("user already voted"));
            responseDto.setStatus(HttpStatus.FORBIDDEN.value());
            return new ResponseEntity<>(responseDto, HttpStatus.FORBIDDEN);
        }

        // create select
        SelectEntity select = new SelectEntity();
        select.setVoted(voted.isVoted());
        select.setGamePlayer(gamePlayer);
        select = selectRepository.save(select);
        lastAdventure.getSelects().add(select);

        // check vote is end
        if (lastAdventure.isEnd()) {
            lastAdventure.setResult();
            sseService.adventureSelectEnd(gameId);
            adventureEnd(game);
        }

        // return
        responseDto.setMessage(String.format("select is appended to vote"));
        responseDto.setStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<GetAdventureResponseDto> getAdventure(int adventureId, String userId) {

        GetAdventureResponseDto responseDto = new GetAdventureResponseDto();

        // get adventure
        Optional<AdventureEntity> adventureEntity = adventureRepository.findById(adventureId);
        if (adventureEntity.isEmpty()) {
            responseDto.setMessage(String.format("vote[%s] not found", adventureId));
            responseDto.setStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        AdventureEntity adventure = adventureEntity.get();

        // check user auth
//        Optional<GamePlayerEntity> gamePlayerOptional = gamePlayerRepository.findById(userId);
//        if (gamePlayerOptional.isEmpty() || game.getWaitingRoom().containsUser(userId)){
//            responseDto.setMessage(String.format("user[%s] is not member", userId));
//            responseDto.setStatus(HttpStatus.UNAUTHORIZED.value());
//            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
//        }

        // check vote is empty
//        if (game.getVotes().isEmpty()){
//            responseDto.setMessage(String.format("request success"));
//            responseDto.setStatus(HttpStatus.OK.value());
//            return new ResponseEntity<>(responseDto, HttpStatus.OK);
//        }

        responseDto.setData(adventure);
        responseDto.setMessage(String.format("request success"));
        responseDto.setStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    public ResponseEntity<DefaultResponseDto> assassinPick(String gameId, String userId, AssassinPick assassinPick) {

        DefaultResponseDto responseDto = new DefaultResponseDto();

        // check game is empty
        Optional<GameEntity> gameOptional = gameRepository.findById(gameId);
        if (gameOptional.isEmpty()) {
            responseDto.setMessage(String.format("game[%s] not found", gameId));
            responseDto.setStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        GameEntity game = gameOptional.get();

        // check user auth
        Optional<GamePlayerEntity> gamePlayerOptional = gamePlayerRepository.findById(userId);
        if (gamePlayerOptional.isEmpty() || !game.getWaitingRoom().containsUser(userId)) {
            responseDto.setMessage(String.format("user[%s] is not member", userId));
            responseDto.setStatus(HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }
        GamePlayerEntity gamePlayer = gamePlayerOptional.get();

        // check user is assassin
        if (gamePlayer.getCharacter().getId() != 4) {
            responseDto.setMessage(String.format("user[%s] is not assassin", userId));
            responseDto.setStatus(HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }

        // check selected user is valid
        if (!game.getWaitingRoom().containsUser(assassinPick.getAssassin_pick())) {
            responseDto.setMessage(String.format("selected user[%s] is not game member", assassinPick.getAssassin_pick()));
            responseDto.setStatus(HttpStatus.FORBIDDEN.value());
            return new ResponseEntity<>(responseDto, HttpStatus.FORBIDDEN);
        }
        GamePlayerEntity selected = gamePlayerRepository.findById(assassinPick.getAssassin_pick()).get();

        // check adventure success
        //

        game.setAssassin_pick(selected);
        game = gameRepository.save(game);

        assassinPickEnd(game);

        responseDto.setMessage("assassin pick is successfully registered");
        responseDto.setStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<GetAssassinPickResponseDto> getAssassinPick(String gameId, String userId) {

        GetAssassinPickResponseDto responseDto = new GetAssassinPickResponseDto();

        // check game is empty
        Optional<GameEntity> gameOptional = gameRepository.findById(gameId);
        if (gameOptional.isEmpty()) {
            responseDto.setMessage(String.format("game[%s] not found", gameId));
            responseDto.setStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        GameEntity game = gameOptional.get();

        // check user auth
        Optional<GamePlayerEntity> gamePlayerOptional = gamePlayerRepository.findById(userId);
        if (gamePlayerOptional.isEmpty() || !game.getWaitingRoom().containsUser(userId)) {
            responseDto.setMessage(String.format("user[%s] is not member", userId));
            responseDto.setStatus(HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }
        GamePlayerEntity gamePlayer = gamePlayerOptional.get();

       responseDto.setData(game.getAssassin_pick().getId());
        responseDto.setMessage("assassin pick is successfully registered");
        responseDto.setStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public void voteEnd(GameEntity game, VoteEntity vote){
        // success_start adventure
        if (vote.getResult()) {
            game.setVotes_fail_cnt(0);
        }
        // fail_next turn
        else {
            game.setVotes_fail_cnt(game.getVotes_fail_cnt()+1);
            // check is game end
            if (game.getVotes_fail_cnt() >= 5) {
                game.setResult(false);
                sseService.voteFailFive(game.getId());
            }

            // next turn
            game.setTerm_cnt(game.getTerm_cnt()+1);
            if (game.getLeader_idx() == game.getMember().size()-1)
                game.setLeader_idx(0);
            else
                game.setLeader_idx(game.getLeader_idx()+1);
        }
    }

    public void adventureEnd(GameEntity game){
        // check win
        int totalCnt = 0;
        int winCnt = 0;
        for (AdventureEntity adventure : game.getAdventures()){
            totalCnt++;
            if(adventure.getResult())    winCnt++;
        }

        // win 3times
        if (winCnt >= 3){
            sseService.adventureTotalSuccess(game.getId());
        }

        // fail while 5 times
        if (winCnt < 3 && totalCnt >= 5){
            game.setResult(false);
            sseService.adventureTotalFail(game.getId());
        }

        // next turn
        game.setVotes_fail_cnt(game.getVotes_fail_cnt()+1);
        game.setTerm_cnt(game.getTerm_cnt()+1);
        if (game.getLeader_idx() == game.getMember().size()-1)
            game.setLeader_idx(0);
        else
            game.setLeader_idx(game.getLeader_idx()+1);
    }

    public void assassinPickEnd(GameEntity game){
        // 악 승리(어세신이 맞춤)
        if (game.getAssassin_pick().getCharacter().getId() == 4){
            game.setResult(false);
        }
        // 악(어쎄신이 틀림)
        else{
            game.setResult(true);
        }
        sseService.assassinPickEnd(game.getId());
    }
}
