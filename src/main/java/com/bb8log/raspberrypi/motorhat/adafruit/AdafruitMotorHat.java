package com.bb8log.raspberrypi.motorhat.adafruit;

import com.bb8log.raspberrypi.motorhat.exception.MotorException;
import com.bb8log.raspberrypi.motorhat.motor.DCMotor;
import com.bb8log.raspberrypi.motorhat.motor.StepperMotor;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;

import java.io.IOException;

/**
 * Created by Florian on 27/02/2016.
 */
public class AdafruitMotorHat {
    private I2CDevice i2CDevice;
    private PWM pwm;
    private int address = 0x60;
    private int frequency = 1600;
    private DCMotor[] motors = new DCMotor[4];
    private StepperMotor[] steppers = new StepperMotor[2];
    private boolean debug = false;

    public AdafruitMotorHat(I2CBus i2CBus) throws IOException {
        this(i2CBus, 0x60, 1600, false);
    }

    public AdafruitMotorHat(I2CBus i2CBus, boolean debug) throws IOException {
        this(i2CBus, 0x60, 1600, debug);
    }

    public AdafruitMotorHat(I2CBus i2CBus, int frequency, boolean debug) throws IOException {
        this(i2CBus, 0x60, frequency, debug);
    }

    public AdafruitMotorHat(I2CBus i2CBus, int address, int frequency, boolean debug) throws IOException {
        this.address = address;
        this.frequency = frequency;
        for (int i = 0; i < 4; i++) {
            motors[i] = new DCMotor(this, i + 1);
        }
        this.steppers[0] = new StepperMotor(this, 1);
        this.steppers[1] = new StepperMotor(this, 2);
        this.i2CDevice = i2CBus.getDevice(address);
        this.debug = debug;
        this.pwm = new PWM(this.i2CDevice, this.address, this.debug);
        this.pwm.setPWMFreq(this.frequency);
    }

    public void setPinValue(int pin, int value) throws MotorException {
        if (pin < 0 || pin > 15) {
            throw new MotorException("PWM pin must be between 0 and 15 inclusive");
        }
        if (value != 0 && value != 1) {
            throw new MotorException("Pin value must be 0 or 1!");
        }
        if (value == 0) {
            this.pwm.setPWM(pin, 0, 4096);
        }
        if (value == 1) {
            this.pwm.setPWM(pin, 4096, 0);
        }
    }

    public StepperMotor getStepper(int num) throws MotorException {
        if (num < 1 || num > 2) {
            throw new MotorException("MotorHAT Stepper must be between 1 and 2 inclusive");
        }
        return steppers[num - 1];
    }

    public DCMotor getDcMotor(int num) throws MotorException {
        if (num < 1 || num > 4) {
            throw new MotorException("MotorHAT Motor must be between 1 and 4 inclusive");
        }
        return motors[num - 1];
    }

    public PWM getPwm() {
        return pwm;
    }
}
