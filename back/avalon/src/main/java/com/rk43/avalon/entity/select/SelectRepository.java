package com.rk43.avalon.entity.select;

import com.rk43.avalon.entity.game_player.GamePlayerEntity;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;

@Repository
public class SelectRepository {
    long lastId = 1;
    HashMap<Long, SelectEntity> selectMap = new HashMap<Long, SelectEntity>();

    public SelectEntity save(SelectEntity select){
        // update
        if (selectMap.containsKey(select.getId())){
            selectMap.put(select.getId(), select);
            return select;
        }
        // create
        else{
            select.setId(lastId++);
            selectMap.put(select.getId(), select);
            return select;
        }
    }

    public Optional<SelectEntity> findById(long id){
        if (selectMap.containsKey(id))
            return Optional.of(selectMap.get(id));
        return Optional.empty();
    }

    public void deleteById(long id){
        try{
            selectMap.remove(id);
        } catch (Exception ignored){}
    }
}
