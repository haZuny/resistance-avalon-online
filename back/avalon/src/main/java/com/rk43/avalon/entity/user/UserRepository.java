package com.rk43.avalon.entity.user;

import org.apache.catalina.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

@Repository
public class UserRepository {
    // data
    HashMap<String, UserEntity> userMap = new HashMap<>();

    // about generate key
    Random random = new Random();

    UserEntity save(UserEntity user){
        String id = createId();
        user.setId(id);
        userMap.put(id, user);
        return user;
    }

    Optional<UserEntity> findById(String id){
        if(userMap.containsKey(id)) return Optional.ofNullable(userMap.get(id));
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
        } while (!userMap.containsKey(id));
        return id;
    }
}
