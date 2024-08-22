package com.rk43.avalon.entity.game.dto;

import com.rk43.avalon.entity.DefaultResponseDto;
import com.rk43.avalon.entity.select.SelectEntity;
import com.rk43.avalon.entity.vote.VoteEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class GetVotesResponseDto extends DefaultResponseDto {

    Data data;

    public void setData(VoteEntity vote){
        data.vote_id = vote.getId();
        data.vote_result = vote.isResult();
        data.vote_selects = new ArrayList<>();
        for (SelectEntity select : vote.getSelects()){
            data.getVote_selects().add(new Select(select.getId(), select.getGamePlayer().getId(), select.isVoted()));
        }
    }

    @Getter
    @Setter
    class Data{
        long vote_id;
        ArrayList<Select> vote_selects;
        boolean vote_result;
    }
}
