package com.rk43.avalon.entity.game;

import com.rk43.avalon.entity.DefaultResponseDto;
import com.rk43.avalon.entity.game.dto.CreateGameResponseDto;
import com.rk43.avalon.entity.game.dto.CreateNewVoteResponseDto;
import com.rk43.avalon.entity.game.dto.SelectRequestDto;
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

    @PostMapping("/{gameId}")
    public ResponseEntity<CreateGameResponseDto> createGame(@PathVariable String gameId, @RequestParam(name = "user-id") String userId){
        return gameService.createNewGame(gameId, userId);
    }

    @PostMapping("/{gameId}/votes")
    public ResponseEntity<CreateNewVoteResponseDto> createNewVote(@PathVariable String gameId, @RequestParam(name = "user-id") String userId){
        return gameService.createNewVote(gameId, userId);
    }

    @PatchMapping("/{gameId}/votes")
    public ResponseEntity<DefaultResponseDto> addVoteSelect(@PathVariable String gameId,
                                                            @RequestParam(name = "user-id") String userId,
                                                            @RequestBody SelectRequestDto voted){
        return gameService.addVoteSelect(gameId, userId, voted);
    }

}
