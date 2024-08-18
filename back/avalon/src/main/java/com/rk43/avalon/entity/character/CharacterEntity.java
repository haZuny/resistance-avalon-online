package com.rk43.avalon.entity.character;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CharacterEntity {
    int id;
    String character;

    public CharacterEntity(int id, String character) {
        this.id = id;
        this.character = character;
    }
}
