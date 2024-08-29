package com.rk43.avalon.entity.select;

import com.rk43.avalon.entity.game_player.GamePlayerEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectEntity {
    long id = -1;
    GamePlayerEntity gamePlayer;
    boolean voted;
}
