package com.meters.shooty;

/**
 * Created by Meters on 2/7/16.
 */
public class Path {

    public static final int TYPE_STRAIGHT = 0;
    public static final int TYPE_ARC = 1;

    int type;

    float speed;

    float oriX;
    float oriY;

    float speedX;
    float speedY;

    float radius;


    float anchorX;
    float anchorY;

    int clock;

    public Path(float oriX, float oriY){
        type = TYPE_STRAIGHT;
        this.oriX = oriX;
        this.oriY = oriY;
    }

    public Path(float radius, float oriX, float oriY){
        type = TYPE_ARC;
        this.radius = radius;
        this.oriX = oriX;
        this.oriY = oriY;
    }

    public void tick(){
        clock++;

        if(type == TYPE_ARC){
            speedX = (float)Math.cos(clock) * oriX;
            speedY = (float)Math.sin(clock) * oriY;
        }
        else{
            speedX = oriX;
            speedY = oriY;
        }


    }

}
