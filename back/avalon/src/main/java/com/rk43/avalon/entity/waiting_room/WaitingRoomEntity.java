package com.rk43.avalon.entity.waiting_room;

import com.rk43.avalon.entity.user.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.User;

import java.util.ArrayList;

@Getter
@Setter
public class WaitingRoomEntity {
    String id;
    UserEntity admin;
    ArrayList<UserEntity> member;

}
