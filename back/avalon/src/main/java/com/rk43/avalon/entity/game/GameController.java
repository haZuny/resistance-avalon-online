package com.rk43.avalon.entity.game;

import com.rk43.avalon.entity.game.dto.CreateGameResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

}
