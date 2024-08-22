package com.rk43.avalon.entity.game.dto;

import com.rk43.avalon.entity.DefaultResponseDto;
import lombok.Getter;
import lombok.Setter;

public class CreateNewVoteResponseDto extends DefaultResponseDto {
    Data data;

    public void setData(long voteId){
        data = new Data();
        data.setVote_id(voteId);
    }

    @Getter
    @Setter
    class Data{
        long vote_id;
    }
}
