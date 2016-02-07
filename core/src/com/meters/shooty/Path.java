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
    int offset;

    float anchorX;
    float anchorY;

    int clock;

    float directionX;
    float directionY;


    public Path(float oriX, float oriY){
        type = TYPE_STRAIGHT;
        this.oriX = oriX;
        this.oriY = oriY;
    }

    public Path(float radius, float speed, float directionX, float directionY, int offset){
        type = TYPE_ARC;
        this.radius = radius;
        this.speed = speed;
        this.offset = offset;

        this.directionX = directionX;
        this.directionY = directionY;
    }

    public void tick(){
        clock++;

        if(type == TYPE_ARC){

            //toa cah soh

            float dG = (float) Math.asin(speed / radius) / 2f * (clock + offset);

            speedX = (float)Math.sin(dG) * speed * directionX;
            speedY = (float)Math.cos(dG) * speed * directionY;
        }
        else{
            speedX = oriX;
            speedY = oriY;
        }


    }

}
