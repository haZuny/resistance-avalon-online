package com.rk43.avalon.entity.waiting_room;

import com.rk43.avalon.entity.character.CharacterEntity;
import com.rk43.avalon.entity.user.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;

@Getter
@Setter
@Component
public class WaitingRoomEntity {
    private String id;
    private UserEntity admin;
    private ArrayList<UserEntity> member = new ArrayList<>();
    private ArrayList<CharacterEntity> selectedCharacter = new ArrayList<>();
    private int maximumUser;

    public boolean containsUser(String userId){
        if (admin.getId().equals(userId))   return true;
        for (UserEntity user : member){
            if (user.getId().equals(userId)) return true;
        }
        return false;
    }

    // refresh selected by member
    public void refreshCharacters() {

        // check selected
        boolean selectedMordred = false;
        boolean selectedOberon = false;
        for (CharacterEntity character : selectedCharacter){
            if (character.getId() == 6) selectedMordred = true;
            else if(character.getId() == 7) selectedOberon = true;
        }

        // set selectable
            // if member withdraw and selected character cnt is bigger than game rule, set selected character
        if (member.size() + 1 <= 6){
            selectedCharacter = new ArrayList<>();
        }
        else if (member.size() + 1 <= 9){
            if (selectedMordred && selectedOberon) {
                selectedCharacter.removeIf(character -> character.getId() != 6);
            }
        }
    }
}
