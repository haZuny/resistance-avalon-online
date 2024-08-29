package com.rk43.avalon.entity.game.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GamePlayer {
    String game_player_id;
    String game_player_nickname;
    String game_player_character;

    public GamePlayer(String game_player_id, String game_player_nickname, String game_player_character) {
        this.game_player_id = game_player_id;
        this.game_player_nickname = game_player_nickname;
        this.game_player_character = game_player_character;
    }
}
