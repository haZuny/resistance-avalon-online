package com.rk43.avalon.entity.character;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;

@Repository
public class CharacterRepository {
    HashMap<Integer, CharacterEntity> characterMap = new HashMap<>();

    public CharacterRepository() {
        // add characters
            // good
        this.characterMap.put(0, new CharacterEntity(0, "good_default"));
        this.characterMap.put(1, new CharacterEntity(1, "merlin"));
        this.characterMap.put(2, new CharacterEntity(2, "percival"));
            // evil
        this.characterMap.put(3, new CharacterEntity(3, "evil_default"));
        this.characterMap.put(4, new CharacterEntity(4, "assassin"));
        this.characterMap.put(5, new CharacterEntity(5, "morgana"));
        this.characterMap.put(6, new CharacterEntity(6, "mordred"));
        this.characterMap.put(7, new CharacterEntity(7, "oberon"));
    }

    Optional<CharacterEntity> findById(int id){
        if (characterMap.containsKey(id))   return Optional.ofNullable(characterMap.get(id));
        else return Optional.empty();
    }

    Optional<CharacterEntity> findByCharacter(String character){
        Iterator<HashMap> iter = characterMap.iter
    }
}
