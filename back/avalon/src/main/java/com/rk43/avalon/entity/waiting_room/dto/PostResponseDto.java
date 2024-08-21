package com.rk43.avalon.entity.waiting_room.dto;

import com.rk43.avalon.entity.DefaultResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class PostResponseDto extends DefaultResponseDto {
    WaitingRoomData data;
}
