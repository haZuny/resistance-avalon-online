package com.rk43.avalon.entity.game.dto;

import com.rk43.avalon.entity.DefaultResponseDto;
import com.rk43.avalon.entity.adventure.AdventureEntity;
import com.rk43.avalon.entity.game.GameEntity;
import com.rk43.avalon.entity.game_player.GamePlayerEntity;
import com.rk43.avalon.entity.select.SelectEntity;
import com.rk43.avalon.entity.vote.VoteEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class GetGameResponseDto extends DefaultResponseDto {
    Data data;

    @Getter
    @Setter
    public class Data{
        String game_id;
        ArrayList<GamePlayer> game_member;
        String game_leader;
        ArrayList<Adventure> game_adventures;
        ArrayList<Vote> game_votes;
        int game_votes_fail_cnt;
        int game_term_cnt;
        String game_assassin_pick;
        Boolean game_result;
    }

    public void setData(GameEntity game){
        data = new Data();
        data.game_id = game.getId();
        data.game_member = new ArrayList<>();
        for (GamePlayerEntity gamePlayer : game.getMember()){
            data.game_member.add(new GamePlayer(gamePlayer.getId(),
                    gamePlayer.getUser().getNickname(),
                    gamePlayer.getCharacter().getCharacter()));
        }
        data.game_leader = game.getMember().get(game.getLeader_idx()).getId();
        data.game_adventures = new ArrayList<>();
        for (AdventureEntity adventure : game.getAdventures()){
            Adventure adventureDto = new Adventure();
            adventureDto.adventure_id = adventure.getId();
            adventureDto.adventure_result = adventure.getResult();
            adventureDto.adventure_selects = new ArrayList<>();
            for (SelectEntity select : adventure.getSelects()){
                adventureDto.adventure_selects.add(new Select(select.getId(), select.getGamePlayer().getId(), select.isVoted()));
            }
            adventureDto.adventure_member = new ArrayList<>();
            for (GamePlayerEntity gamePlayer : adventure.getMember()){
                adventureDto.adventure_member.add(gamePlayer.getId());
            }
            data.game_adventures.add(adventureDto);
        }
        data.game_votes = new ArrayList<>();
        for (VoteEntity vote : game.getVotes()){
            Vote voteDto = new Vote();
            voteDto.vote_id = vote.getId();
            voteDto.vote_result = vote.getResult();
            voteDto.vote_selects = new ArrayList<>();
            for (SelectEntity select : vote.getSelects()){
                voteDto.getVote_selects().add(new Select(select.getId(), select.getGamePlayer().getId(), select.isVoted()));
            }
            data.game_votes.add(voteDto);
        }
        data.game_votes_fail_cnt= game.getVotes_fail_cnt();
        data.game_term_cnt = game.getTerm_cnt();
        if (game.getAssassin_pick() != null)
            data.game_assassin_pick = game.getAssassin_pick().getId();
        if (game.getResult() != null)
            data.game_result = game.getResult();
    }
}
