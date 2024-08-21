package com.rk43.avalon.entity.waiting_room.dto;

import com.rk43.avalon.entity.character.CharacterEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class WaitingRoomData {
    private String waiting_room_id;
    private UserData waiting_room_admin;
    private ArrayList<UserData> waiting_room_member;
    private int waiting_room_maximum_user;
    private ArrayList<String> waiting_room_selected_character;
    private ArrayList<String> waiting_room_selectable_character;

    public void refreshCharacters(ArrayList<CharacterEntity> selectedCharacters) throws Exception {
        // cnt
        int cnt_good = 0;
        int cnt_evil = 0;
        boolean selected_mordred = false;
        boolean selected_oberon = false;

        // set selected character
        waiting_room_selected_character = new ArrayList<>();
        for (CharacterEntity character : selectedCharacters){
            waiting_room_selected_character.add(character.getCharacter());
            if (character.isGood()) cnt_good+=1;
            else    cnt_evil+=1;
            if(character.getId()==6)    selected_mordred = true;
            if(character.getId()==7)    selected_oberon = true;
        }

        // set selectable
        waiting_room_selectable_character = new ArrayList<>();
        // can not select
        if (waiting_room_member.size() == 4 || waiting_room_member.size() == 5){
            // err case
            if (selected_mordred || selected_oberon){
                throw new Exception("selected character is invaild");
            }
        }
        // can select 1
        else if(waiting_room_member.size() == 6
                || waiting_room_member.size() == 7
                || waiting_room_member.size() == 8){

            if (!selected_oberon && !selected_mordred){
                waiting_room_selectable_character.add("mordred");
                waiting_room_selectable_character.add("oberon");
            }
            // err case
            else if (selected_oberon && selected_mordred){
                throw new Exception("selected character is invaild");
            }
        }
        // can select 2
        else if(waiting_room_member.size() == 9){
            if (!selected_oberon)    waiting_room_selectable_character.add("oberon");
            if (!selected_mordred)  waiting_room_selectable_character.add("mordred");
        }
        // err case
        else{
            throw new Exception("selected character is invaild");
        }
    }
}
