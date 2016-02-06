package com.meters.shooty;

/**
 * Created by Meters on 2/7/2016.
 */
public class Step {

    int tick;
    float speedX;
    float speedY;
    boolean fire;

    public Step(int tick, float speedX, float speedY, boolean fire){
        this.tick = tick;
        this.speedX = speedX;
        this.speedY = speedY;
        this.fire = fire;
    }

}
