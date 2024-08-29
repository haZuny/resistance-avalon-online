package com.rk43.avalon.entity.game.dto;

import com.rk43.avalon.entity.DefaultResponseDto;
import com.rk43.avalon.entity.adventure.AdventureEntity;
import com.rk43.avalon.entity.game_player.GamePlayerEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class CreateAdventureResponseDto extends DefaultResponseDto {

    Data data;

    public void setData(AdventureEntity adventure){
        data = new Data();
        data.setAdventure_id(adventure.getId());
        data.setAdventure_member(new ArrayList<>());
        for (GamePlayerEntity gamePlayer : adventure.getMember()){
            data.getAdventure_member().add(gamePlayer.getId());
        }
    }

    @Getter
    @Setter
    public class Data{
        long adventure_id;
        ArrayList<String> adventure_member;
    }
}
