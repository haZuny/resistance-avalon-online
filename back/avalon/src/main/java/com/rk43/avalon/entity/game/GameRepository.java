package com.rk43.avalon.entity.game;

import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.HashMap;
import java.util.Optional;

@Repository
public class GameRepository {
    HashMap<String, GameEntity> gameMap = new HashMap<>();

    GameEntity save(GameEntity game){
        game.setId(game.getWaitingRoom().getId());
        gameMap.put(game.getId(), game);
        return game;
    }

    Optional<GameEntity> findById(String id){
        if (gameMap.containsKey(id))
            return Optional.of(gameMap.get(id));
        return Optional.empty();
    }

    public void deleteById(String id){
        try{
            gameMap.remove(id);
        } catch (Exception ignored){}
    }
}
