package com.bb8log.raspberrypi.adafruitmotorhat.motor.dc;

import com.bb8log.raspberrypi.adafruitmotorhat.AdafruitMotorHat;
import com.bb8log.raspberrypi.adafruitmotorhat.exception.MotorException;
import com.bb8log.raspberrypi.adafruitmotorhat.motor.AbstractMotor;
import com.bb8log.raspberrypi.adafruitmotorhat.motor.MotorCommand;

/**
 * Created by Florian on 27/02/2016.
 */
public class DCMotor extends AbstractMotor {

    private DCMotorInfos infos;

    public DCMotor(AdafruitMotorHat controller, int num) {
        super(controller, num);
        this.infos = DCMotorInfos.getByNum(num - 1);
    }

    public void run(MotorCommand command) throws MotorException {
        if (infos == null) {
            return;
        }
        if (command == MotorCommand.FORWARD) {
            getController().setPinValue(infos.in2, 0);
            getController().setPinValue(infos.in1, 1);
        }
        if (command == MotorCommand.BACKWARD) {
            getController().setPinValue(infos.in1, 0);
            getController().setPinValue(infos.in2, 1);
        }
        if (command == MotorCommand.RELEASE) {
            getController().setPinValue(infos.in1, 0);
            getController().setPinValue(infos.in2, 0);
        }
    }

    public void setSpeed(int speed) {
        if (speed < 0)
            speed = 0;
        if (speed > 255)
            speed = 255;
        getController().getPwm().setPWM(infos.pwm, 0, speed * 16);
    }
}
