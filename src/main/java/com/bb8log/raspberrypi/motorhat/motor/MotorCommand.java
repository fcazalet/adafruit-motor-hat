package com.bb8log.raspberrypi.motorhat.motor;

/**
 * Created by Florian on 28/02/2016.
 */
public enum MotorCommand {
    FORWARD(1), BACKWARD(2), BRAKE(3), RELEASE(4);

    public int code;


    MotorCommand(int code) {
        this.code = code;
    }
}
