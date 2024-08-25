package com.rk43.avalon.entity.game.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Vote {
    long vote_id;
    ArrayList<Select> vote_selects;
    Boolean vote_result;
}
