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
    Boolean result;

    public void setResult(GameEntity game){
        int trueNum = 0;
        for (SelectEntity select : selects){
            if (select.isVoted())   trueNum++;
        }
        // 4번째 원정 특이점
        if (game.getMember().size() >= 7 && game.getAdventures().size() == 4){
            result = selects.size()-trueNum < 2;
        }
        // 그 외에는 만장일치
        else{
            result = trueNum == selects.size();
        }
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
