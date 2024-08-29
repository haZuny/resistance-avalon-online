package com.rk43.avalon.entity.character;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

//@Component
@Getter
@Setter
public class CharacterEntity {
    int id;
    String character;
    boolean isGood;

    public CharacterEntity(int id, String character, boolean isGood) {
        this.id = id;
        this.character = character;
        this.isGood = isGood;
    }
}
