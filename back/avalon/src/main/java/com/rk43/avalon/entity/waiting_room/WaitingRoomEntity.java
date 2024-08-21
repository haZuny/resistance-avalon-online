package com.rk43.avalon.entity.waiting_room;

import com.rk43.avalon.entity.character.CharacterEntity;
import com.rk43.avalon.entity.user.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Getter
@Setter
@Component
public class WaitingRoomEntity {
    private String id;
    private UserEntity admin;
    private ArrayList<UserEntity> member = new ArrayList<>();
    private ArrayList<CharacterEntity> selectedCharacter = new ArrayList<>();
    private int maximumUser;
    private int lastMemberOrder;

    public boolean containsUser(String userId){
        if (admin.getId().equals(userId))   return true;
        for (UserEntity user : member){
            if (user.getId().equals(userId)) return true;
        }
        return false;
    }
}
