package com.meters.shooty;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

/**
 * Created by Meters on 2/5/2016.
 */
public class Tile {

    public static int STATE_PLAYING = 0;
    public static int STATE_BLINKING = 1;
    public static int STATE_DED = 2;

    ArrayList<Step> steps;
    Step currentStep;
    int stepCount;

    Rectangle rect;
    Color c;
    float speed;

    float speedX;
    float speedY;
    boolean toFire;

    int clock;
    int state;
    int health;

    public void tick(){
        clock++;
        if(steps.size() > stepCount){
            Step getStep = steps.get(stepCount);
            if(getStep.tick <= clock){
                currentStep = getStep;
                speedX = currentStep.speedX;
                speedY = currentStep.speedY;
                toFire = currentStep.fire;
                stepCount++;
            }
        }
        rect.x += speedX;
        rect.y += speedY;
    }

}
