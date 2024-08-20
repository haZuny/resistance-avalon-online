package com.rk43.avalon.entity.waiting_room.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserData {
    String id;
    String nickname;

    public UserData(String id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
