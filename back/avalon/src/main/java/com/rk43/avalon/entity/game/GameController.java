package com.rk43.avalon.entity.game;

import com.rk43.avalon.entity.DefaultResponseDto;
import com.rk43.avalon.entity.game.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
public class GameController {
    GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * About Game
     */
    @PostMapping("/{gameId}")
    public ResponseEntity<CreateGameResponseDto> createGame(@PathVariable String gameId, @RequestParam(name = "user-id") String userId) {
        return gameService.createNewGame(gameId, userId);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GetGameResponseDto> getGame(@PathVariable String gameId
            , @RequestParam(name = "user-id") String userId) {
        return gameService.getGame(gameId, userId);
    }

    /**
     * About vote
     */
    @PostMapping("/{gameId}/votes")
    public ResponseEntity<CreateNewVoteResponseDto> createNewVote(@PathVariable String gameId, @RequestParam(name = "user-id") String userId) {
        return gameService.createNewVote(gameId, userId);
    }

    @GetMapping("/{gameId}/votes")
    public ResponseEntity<GetVoteResponseDto> getVotes(@PathVariable String gameId, @RequestParam(name = "user-id") String userId) {
        return gameService.getVotes(gameId, userId);
    }

    @PatchMapping("/{gameId}/votes")
    public ResponseEntity<DefaultResponseDto> addVoteSelect(@PathVariable String gameId,
                                                            @RequestParam(name = "user-id") String userId,
                                                            @RequestBody SelectRequestDto voted) {
        return gameService.addVoteSelect(gameId, userId, voted);
    }

    @GetMapping("/votes/{voteId}")
    public ResponseEntity<GetVoteResponseDto> getVote(@PathVariable int voteId,
                                                      @RequestParam(name = "user-id") String userId) {
        return gameService.getVote(voteId, userId);
    }

    /**
     * About adventure
     */
    @PostMapping("/{gameId}/adventures")
    public ResponseEntity<CreateAdventureResponseDto> createAdventure(@PathVariable String gameId,
                                                                      @RequestParam(name = "user-id") String userId,
                                                                      @RequestBody AdventureMember member) {
        return gameService.createAdventure(gameId, userId, member);
    }

    @GetMapping("/{gameId}/adventures")
    public ResponseEntity<GetAdventureResponseDto> getAdventures(@PathVariable String gameId, @RequestParam(name = "user-id") String userId) {
        return gameService.getAdventure(gameId, userId);
    }

    @PatchMapping("/{gameId}/adventures")
    public ResponseEntity<DefaultResponseDto> addAdventureSelect(@PathVariable String gameId,
                                                                 @RequestParam(name = "user-id") String userId,
                                                                 @RequestBody SelectRequestDto voted) {
        return gameService.addAdventueSelect(gameId, userId, voted);
    }

    @GetMapping("/adventures/{adventureId}")
    public ResponseEntity<GetAdventureResponseDto> getAdventure(@PathVariable int adventureId,
                                                                @RequestParam(name = "user-id") String userId) {
        return gameService.getAdventure(adventureId, userId);
    }

    /**
     * About Assassin
     */
    @PatchMapping("/{gameId}/assassin-pick")
    public ResponseEntity<DefaultResponseDto> pickAssassin(@PathVariable String gameId,
                                                           @RequestParam(name = "user-id") String userId,
                                                           @RequestBody AssassinPick assassinPick){
        return gameService.assassinPick(gameId, userId, assassinPick);
    }




}
