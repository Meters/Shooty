package com.meters.shooty;

/**
 * Created by Meters on 2/7/2016.
 */
public class Step {

    int tick;
    boolean fire;

    Path path;

    public Step(int tick, Path path, boolean fire){
        this.tick = tick;
        this.path = path;
        this.fire = fire;
    }

}
