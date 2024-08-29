package com.rk43.avalon.entity.game.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Adventure {
    long adventure_id;
    ArrayList<String> adventure_member;
    ArrayList<Select> adventure_selects;
    Boolean adventure_result;
}
