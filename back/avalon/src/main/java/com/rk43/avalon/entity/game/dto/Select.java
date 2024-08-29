package com.rk43.avalon.entity.game.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Select {
    long select_id;
    String select_user;
    boolean select_voted;

    public Select(long select_id, String select_user, boolean select_voted) {
        this.select_id = select_id;
        this.select_user = select_user;
        this.select_voted = select_voted;
    }
}
