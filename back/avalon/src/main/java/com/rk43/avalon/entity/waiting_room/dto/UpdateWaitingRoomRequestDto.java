package com.rk43.avalon.entity.waiting_room.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class UpdateWaitingRoomRequestDto {
    ArrayList<String> selected_character;
    int maximum_user = -1;
}
