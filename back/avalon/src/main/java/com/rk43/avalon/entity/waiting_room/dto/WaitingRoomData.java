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

        waiting_room_selected_character = new ArrayList<>();

        // check selected
        boolean selectedMordred = false;
        boolean selectedOberon = false;
        for (CharacterEntity character : selectedCharacters){
            waiting_room_selected_character.add(character.getCharacter());
            if (character.getId() == 6) selectedMordred = true;
            else if(character.getId() == 7) selectedOberon = true;
        }

        // set selectable
        waiting_room_selectable_character = new ArrayList<>();
        if (waiting_room_member.size() + 1 <= 6){
            if (selectedMordred || selectedOberon)  throw new Exception("err");
        }
        else if (waiting_room_member.size() + 1 <= 9){
            if (selectedMordred && selectedOberon)  throw new Exception("err");
            else if (selectedMordred)   {}
            else if (selectedOberon)    {}
            else{
                waiting_room_selectable_character.add("mordred");
                waiting_room_selectable_character.add("oberon");
            }
        }
        else{
            if (selectedMordred && selectedOberon)  {}
            else if (selectedMordred)   waiting_room_selectable_character.add("oberon");
            else if (selectedOberon)    waiting_room_selectable_character.add("mordred");
            else{
                waiting_room_selectable_character.add("mordred");
                waiting_room_selectable_character.add("oberon");
            }
        }
    }
}
