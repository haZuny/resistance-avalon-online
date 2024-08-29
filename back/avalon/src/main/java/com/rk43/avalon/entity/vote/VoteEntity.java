package com.rk43.avalon.entity.vote;

import com.rk43.avalon.entity.game.GameEntity;
import com.rk43.avalon.entity.game_player.GamePlayerEntity;
import com.rk43.avalon.entity.select.SelectEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class VoteEntity {
    long id = -1;
    ArrayList<SelectEntity> selects;
    Boolean result;

    public void setResult(){
        int res = 0;
        for (SelectEntity select : selects){
            if (select.isVoted())   res++;
            else res --;
        }
        if (res > 0)    result = true;
        else result = false;
    }

    public boolean isEnd(GameEntity game){
        if (selects.size() == game.getMember().size()){
            return true;
        }
        return false;
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
