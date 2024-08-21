package com.rk43.avalon.entity.game;

import com.rk43.avalon.entity.adventure.AdventureEntity;
import com.rk43.avalon.entity.game_player.GamePlayerEntity;
import com.rk43.avalon.entity.vote.VoteEntity;
import com.rk43.avalon.entity.waiting_room.WaitingRoomEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class GameEntity {
    String id;
    WaitingRoomEntity waitingRoom;
    ArrayList<GamePlayerEntity> member;
    ArrayList<AdventureEntity> adventures = new ArrayList<>();
    ArrayList<VoteEntity> votes = new ArrayList<>();
    boolean result;
    int leader = 0;
    int votes_fail_cnt = 0;
    int term_cnt = 0;
    String assassin_pick;
}
