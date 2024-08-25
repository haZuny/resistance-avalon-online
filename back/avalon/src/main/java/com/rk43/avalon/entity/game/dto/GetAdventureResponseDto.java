package com.rk43.avalon.entity.game.dto;

import com.rk43.avalon.entity.DefaultResponseDto;
import com.rk43.avalon.entity.adventure.AdventureEntity;
import com.rk43.avalon.entity.game_player.GamePlayerEntity;
import com.rk43.avalon.entity.select.SelectEntity;
import com.rk43.avalon.entity.vote.VoteEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class GetAdventureResponseDto extends DefaultResponseDto {

    Adventure data;

    public void setData(AdventureEntity adventure){
        data = new Adventure();
        data.adventure_id = adventure.getId();
        data.adventure_result = adventure.isResult();
        data.adventure_selects = new ArrayList<>();
        for (SelectEntity select : adventure.getSelects()){
            data.adventure_selects.add(new Select(select.getId(), select.getGamePlayer().getId(), select.isVoted()));
        }
        data.adventure_member = new ArrayList<>();
        for (GamePlayerEntity gamePlayer : adventure.getMember()){
            data.adventure_member.add(gamePlayer.getId());
        }
    }

}
