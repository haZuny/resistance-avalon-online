package com.rk43.avalon.entity.game_player;

import com.rk43.avalon.entity.character.CharacterEntity;
import com.rk43.avalon.entity.user.UserEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GamePlayerEntity {
    String id;
    UserEntity user;
    CharacterEntity character;
}
