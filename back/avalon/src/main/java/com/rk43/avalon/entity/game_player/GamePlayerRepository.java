package com.rk43.avalon.entity.game_player;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;

@Repository
public class GamePlayerRepository {
    HashMap<String, GamePlayerEntity> gamePlayerMap = new HashMap<>();

    public GamePlayerEntity save(GamePlayerEntity gamePlayer){
        gamePlayer.setId(gamePlayer.getUser().getId());
        gamePlayerMap.put(gamePlayer.getId(), gamePlayer);
        return gamePlayer;
    }

    public Optional<GamePlayerEntity> findById(String id){
        if (gamePlayerMap.containsKey(id))
            return Optional.of(gamePlayerMap.get(id));
        return Optional.empty();
    }

    public void deleteById(String id){
        try{
            gamePlayerMap.remove(id);
        } catch (Exception ignored){}
    }
}
