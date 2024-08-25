package com.rk43.avalon.entity.game.dto;

import com.rk43.avalon.entity.DefaultResponseDto;
import com.rk43.avalon.entity.select.SelectEntity;
import com.rk43.avalon.entity.vote.VoteEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class GetVoteResponseDto extends DefaultResponseDto {

    Vote data;

    public void setData(VoteEntity vote){
        data = new Vote();
        data.vote_id = vote.getId();
        data.vote_result = vote.isResult();
        data.vote_selects = new ArrayList<>();
        for (SelectEntity select : vote.getSelects()){
            data.getVote_selects().add(new Select(select.getId(), select.getGamePlayer().getId(), select.isVoted()));
        }
    }

}
