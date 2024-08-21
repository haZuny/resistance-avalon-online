package com.rk43.avalon.entity.vote;

import com.rk43.avalon.entity.select.SelectEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class VoteEntity {
    long id = -1;
    ArrayList<SelectEntity> selects;
    boolean result;
}
