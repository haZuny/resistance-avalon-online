package com.rk43.avalon.entity.waiting_room.dto;

import com.rk43.avalon.entity.DefaultResponseDto;
import com.rk43.avalon.entity.waiting_room.WaitingRoomController;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetIdResponseDto extends DefaultResponseDto {
    WaitingRoomData data;
}
