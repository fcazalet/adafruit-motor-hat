package com.bb8log.raspberrypi.adafruitmotorhat.motor.stepper;

/**
 * Created by Florian on 28/02/2016.
 */
public enum StepStyle {
    SINGLE(1), DOUBLE(2), INTERLEAVE(3), MICROSTEP(4);


    private int code;

    StepStyle(int code) {
        this.code = code;
    }
}
