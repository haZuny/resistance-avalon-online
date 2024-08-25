package com.rk43.avalon.entity.adventure;

import com.rk43.avalon.entity.game.GameEntity;
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

    public void setResult(){
        int res = 0;
        for (SelectEntity select : selects){
            if (select.isVoted())   res++;
            else res --;
        }
        if (res > 0)    result = true;
        else result = false;
    }

    public boolean isEnd(){
        return member.size() == selects.size();
    }

    public boolean userVoted(GamePlayerEntity gamePlayer){
        boolean voted = false;
        for (SelectEntity select : selects){
            if (select.getGamePlayer().getId().equals(gamePlayer.getId())){
                voted = true;
                break;
            }
        }
        return voted;
    }
}
