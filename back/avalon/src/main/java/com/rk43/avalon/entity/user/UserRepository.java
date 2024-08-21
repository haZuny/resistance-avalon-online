package com.rk43.avalon.entity.user;

import org.apache.catalina.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository {
    // data
    HashMap<String, UserEntity> userMap = new HashMap<>();

    // about generate key
    Random random = new Random();

    public UserEntity save(UserEntity user) {
        // create id
        String id = createId();
        user.setId(id);
        // if nickname is empty :: create nickname
        if (user.getNickname().isEmpty()) {
            ArrayList<String> randomNicknameList = new ArrayList<>(List.of(
                    "개똥", "헌터", "마스터", "킹발론", "수호자"
                    , "개맛살", "아맛나", "학살자", "탐정", "킹",
                    "전설의", "레전드", "내이름은", "안녕", "아발론"
                    , "예의바른", "ㅎㅇ", "ㅋㅋ", "오리지날", "게임기"));
            String randomNickname = randomNicknameList.get(random.nextInt(randomNicknameList.size())) + id;
            user.setNickname(randomNickname);
        }
        userMap.put(id, user);
        return user;
    }

    public Optional<UserEntity> findById(String id) {
        if (userMap.containsKey(id)) return Optional.ofNullable(userMap.get(id));
        else return Optional.empty();
    }


    // generate key
    private String createId() {
        String id;
        do {
            id = "";
            for (int i = 0; i < 4; i++) {
                id += Integer.toString(random.nextInt(9));
            }
            System.out.println("띠ㅏ");
        } while (userMap.containsKey(id));
        return id;
    }
}
