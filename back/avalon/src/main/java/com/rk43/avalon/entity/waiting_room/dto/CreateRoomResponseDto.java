package com.rk43.avalon.entity.waiting_room.dto;

import com.rk43.avalon.entity.DefaultResponseDto;
import com.rk43.avalon.entity.user.UserEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRoomResponseDto extends DefaultResponseDto {
    Data data;

    public void setData(String waiting_room_id, UserEntity user) {
        this.data = new Data(waiting_room_id, user);
    }

    @Getter
    @Setter
    public class Data{
        String waiting_room_id;
        UserData waiting_room_admin;

        public Data(String waiting_room_id, UserEntity user){
            this.waiting_room_id = waiting_room_id;
            this.waiting_room_admin = new UserData(user.getId(), user.getNickname());
        }
    }
}
