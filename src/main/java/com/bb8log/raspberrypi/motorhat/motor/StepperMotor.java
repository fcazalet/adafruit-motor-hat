package com.bb8log.raspberrypi.motorhat.motor;

import com.bb8log.raspberrypi.motorhat.adafruit.AdafruitMotorHat;

/**
 * Created by Florian on 27/02/2016.
 */
public class StepperMotor {

    private AdafruitMotorHat controller;
    private int num;

    public StepperMotor(AdafruitMotorHat controller, int num) {
        this.controller = controller;
        this.num = num;
    }
}
