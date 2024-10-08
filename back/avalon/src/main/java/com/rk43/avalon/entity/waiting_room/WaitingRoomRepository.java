package com.rk43.avalon.entity.waiting_room;

import com.rk43.avalon.entity.user.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

@Repository
public class WaitingRoomRepository {
    // data
    HashMap<String, WaitingRoomEntity> waitingRoomMap = new HashMap<>();

    // about generate key
    Random random = new Random();

    // save
    public WaitingRoomEntity save(WaitingRoomEntity waitingRoom){
        // update
        if (waitingRoomMap.containsKey(waitingRoom.getId())){
            waitingRoomMap.put(waitingRoom.getId(), waitingRoom);
            return waitingRoom;
        }
        // create
        else{
            String id = createId();
            waitingRoom.setId(id);
            waitingRoomMap.put(id, waitingRoom);
            return waitingRoom;
        }
    }

    // delete
    public void deleteById(String id){
        try{
            waitingRoomMap.remove(id);
        } catch (Exception e){}
    }

    // find
    public Optional<WaitingRoomEntity> findById(String id){
        if (waitingRoomMap.containsKey(id)) return Optional.of(waitingRoomMap.get(id));
        else return Optional.empty();
    }




    // generate key
    String createId(){
        String id;
        do{
            id = "";
            for (int i = 0; i < 4; i++){
                id += Integer.toString(random.nextInt(9));
            }
        } while (waitingRoomMap.containsKey(id));
        return id;
    }
}
