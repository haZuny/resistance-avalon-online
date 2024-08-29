package com.rk43.avalon.entity.adventure;

import com.rk43.avalon.entity.select.SelectEntity;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;

@Repository
public class AdventureRepository {
    long lastId = 1;
    HashMap<Long, AdventureEntity> adventureMap = new HashMap<Long, AdventureEntity>();

    public AdventureEntity save(AdventureEntity adventure){
        // update
        if (adventureMap.containsKey(adventure.getId())){
            adventureMap.put(adventure.getId(), adventure);
            return adventure;
        }
        // create
        else{
            adventure.setId(lastId++);
            adventureMap.put(adventure.getId(), adventure);
            return adventure;
        }
    }

    public Optional<AdventureEntity> findById(long id){
        if (adventureMap.containsKey(id))
            return Optional.of(adventureMap.get(id));
        return Optional.empty();
    }

    public void deleteById(long id){
        try{
            adventureMap.remove(id);
        } catch (Exception ignored){}
    }
}
