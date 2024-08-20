package com.rk43.avalon.entity.waiting_room.dto;

import com.rk43.avalon.entity.DefaultResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class PostResponseDto extends DefaultResponseDto {

    private String waiting_room_id;
    private UserData waiting_room_admin;
    private ArrayList<UserData> waiting_room_member;
    private int waiting_room_maximum_user;
    private ArrayList<String> waiting_room_playable_character;
    private ArrayList<String> waiting_room_selectable_play_character;
}
