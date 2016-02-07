package com.meters.shooty;

import java.util.ArrayList;

/**
 * Created by Meters on 2/7/16.
 */
public class Spawn {

    int tick;
    float x;
    float y;

    ArrayList<Step> steps;

    public Spawn(int tick, float x, float y){
        this.tick = tick;
        this.x = x;
        this.y = y;
        steps = new ArrayList<Step>();
    }

}
