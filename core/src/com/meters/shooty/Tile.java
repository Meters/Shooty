package com.meters.shooty;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Meters on 2/5/2016.
 */
public class Tile {

    public static int STATE_PLAYING = 0;
    public static int STATE_BLINKING = 1;
    public static int STATE_DED = 2;

    Rectangle rect;
    Color c;
    float speed;

    float speedX;
    float speedY;

    int clock;
    int clock2;

    int state;

    int health;

    public void tick(){

        clock++;
        if((clock2 % 360 < 10 || clock2 % 360 > 350) && clock % 3 == 0){

        }
        else if(clock % 10 == 0){
            clock2++;
        }
        speedX = (float)(speed * 2 * Math.cos(clock2));
        speedY = -(float)(speed * Math.abs(Math.cos(clock2)) / 2);

    }

}
