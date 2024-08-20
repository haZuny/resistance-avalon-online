package com.rk43.avalon.entity.character;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class CharacterEntity {
    int id;
    String character;

    public CharacterEntity(int id, String character) {
        this.id = id;
        this.character = character;
    }
}
