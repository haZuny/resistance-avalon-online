package com.rk43.avalon.entity.adventure;

import com.rk43.avalon.entity.game_player.GamePlayerEntity;
import com.rk43.avalon.entity.select.SelectEntity;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Getter
@Setter
public class AdventureEntity {
    long id = -1;
    ArrayList<GamePlayerEntity> member;
    ArrayList<SelectEntity> selects;
    boolean result;
}
