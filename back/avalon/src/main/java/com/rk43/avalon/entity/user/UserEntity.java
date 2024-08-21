package com.rk43.avalon.entity.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class UserEntity {
    String id;
    String nickname;
}
