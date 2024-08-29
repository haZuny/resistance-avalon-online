package com.rk43.avalon.entity.vote;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;

@Repository
public class VoteRepository {
    long lastId = 1;
    HashMap<Long, VoteEntity> voteMap = new HashMap<Long, VoteEntity>();

    public VoteEntity save(VoteEntity vote){
        // update
        if (voteMap.containsKey(vote.getId())){
            voteMap.put(vote.getId(), vote);
            return vote;
        }
        // create
        else{
            vote.setId(lastId++);
            voteMap.put(vote.getId(), vote);
            return vote;
        }
    }

    public Optional<VoteEntity> findById(long id){
        if (voteMap.containsKey(id))
            return Optional.of(voteMap.get(id));
        return Optional.empty();
    }

    public void deleteById(long id){
        try{
            voteMap.remove(id);
        } catch (Exception ignored){}
    }
}
