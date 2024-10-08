package com.rk43.avalon.entity.game;

import com.rk43.avalon.entity.adventure.AdventureEntity;
import com.rk43.avalon.entity.game_player.GamePlayerEntity;
import com.rk43.avalon.entity.vote.VoteEntity;
import com.rk43.avalon.entity.waiting_room.WaitingRoomEntity;
import com.rk43.avalon.sse.SseService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Getter
@Setter
public class GameEntity {
    String id;
    WaitingRoomEntity waitingRoom;
    ArrayList<GamePlayerEntity> member;
    ArrayList<AdventureEntity> adventures = new ArrayList<>();
    ArrayList<VoteEntity> votes = new ArrayList<>();
    Boolean result;
    int leader_idx = 0;
    int votes_fail_cnt = 0;
    int term_cnt = 0;
    GamePlayerEntity assassin_pick;
}
