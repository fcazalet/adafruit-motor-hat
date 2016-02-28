package com.bb8log.raspberrypi.motorhat.motor;

import com.bb8log.raspberrypi.motorhat.adafruit.AdafruitMotorHat;
import com.bb8log.raspberrypi.motorhat.exception.MotorException;

/**
 * Created by Florian on 27/02/2016.
 */
public class DCMotor {

    private AdafruitMotorHat controller;
    private int num;
    private DCMotorInfos infos;

    public DCMotor(AdafruitMotorHat controller, int num) {
        this.controller = controller;
        this.num = num;
        this.infos = DCMotorInfos.getByNum(num);
    }

    public void run(int command) throws MotorException {
        if (infos == null) {
            return;
        }
        if (command == AdafruitMotorHat.FORWARD) {
            controller.setPinValue(infos.in2, 0);
            controller.setPinValue(infos.in1, 1);
        }
        if (command == AdafruitMotorHat.BACKWARD) {
            controller.setPinValue(infos.in1, 0);
            controller.setPinValue(infos.in2, 1);
        }
        if (command == AdafruitMotorHat.RELEASE) {
            controller.setPinValue(infos.in1, 0);
            controller.setPinValue(infos.in2, 0);
        }
    }

    public void setSpeed(int speed) {
        if (speed < 0)
            speed = 0;
        if (speed > 255)
            speed = 255;
        controller.getPwm().setPWM(infos.pwm, 0, speed * 16);
    }
}
