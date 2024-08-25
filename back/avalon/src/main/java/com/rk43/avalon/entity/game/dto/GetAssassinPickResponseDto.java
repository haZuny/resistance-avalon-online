package com.rk43.avalon.entity.game.dto;

import com.rk43.avalon.entity.DefaultResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAssassinPickResponseDto extends DefaultResponseDto {
    Data data;

    public void setData(String assassinPick){
        data = new Data();
        data.assassin_pick = assassinPick;
    }


    @Getter
    @Setter
    public class Data{
        String assassin_pick;
    }
}
