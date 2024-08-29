package com.rk43.avalon.entity.game.dto;

import com.rk43.avalon.entity.DefaultResponseDto;
import com.rk43.avalon.entity.game.GameEntity;
import com.rk43.avalon.entity.game_player.GamePlayerEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class CreateGameResponseDto extends DefaultResponseDto {

    Data data;

    public void setData(GameEntity game){
        this.data = new Data();
        data.setGame_id(game.getId());
        data.setGame_member(new ArrayList<>());
        for (GamePlayerEntity gamePlayer : game.getMember()){
            data.getGame_member().add(new GamePlayer(gamePlayer.getId(),
                    gamePlayer.getUser().getNickname(),
                    gamePlayer.getCharacter().getCharacter()));
        }
        data.setGame_leader(game.getMember().get(game.getLeader_idx()).getId());
    }

    @Getter
    @Setter
    class Data{
        String game_id;
        ArrayList<GamePlayer> game_member;
        String game_leader;
    }
}
