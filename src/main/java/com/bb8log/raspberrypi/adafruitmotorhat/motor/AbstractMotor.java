package com.bb8log.raspberrypi.adafruitmotorhat.motor;

import com.bb8log.raspberrypi.adafruitmotorhat.AdafruitMotorHat;

/**
 * Created by Florian on 28/02/2016.
 */
public abstract class AbstractMotor {
    private AdafruitMotorHat controller;
    private int num;

    public AbstractMotor(AdafruitMotorHat controller, int num) {
        this.controller = controller;
        this.num = num;
    }

    public AdafruitMotorHat getController() {
        return controller;
    }

    public int getNum() {
        return num;
    }
}
