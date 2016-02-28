package com.bb8log.raspberrypi.motorhat;

import com.bb8log.raspberrypi.motorhat.adafruit.AdafruitMotorHat;
import com.bb8log.raspberrypi.motorhat.exception.MotorException;
import com.bb8log.raspberrypi.motorhat.motor.DCMotor;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

/**
 * Created by Florian on 27/02/2016.
 */
public class Main {
    public static void main(String[] args) throws IOException, MotorException {
        I2CBus i2CBus = I2CFactory.getInstance(1);
        AdafruitMotorHat hat = new AdafruitMotorHat(i2CBus);
        DCMotor dcMotor = hat.getDcMotor(1);
        dcMotor.setSpeed(100);
        try {
            Thread.sleep(2000);                 //1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        dcMotor.setSpeed(0);
    }
}
