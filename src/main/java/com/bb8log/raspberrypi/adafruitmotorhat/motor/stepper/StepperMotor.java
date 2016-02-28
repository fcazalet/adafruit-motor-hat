package com.bb8log.raspberrypi.adafruitmotorhat.motor.stepper;

import com.bb8log.raspberrypi.adafruitmotorhat.AdafruitMotorHat;
import com.bb8log.raspberrypi.adafruitmotorhat.exception.MotorException;
import com.bb8log.raspberrypi.adafruitmotorhat.motor.AbstractMotor;
import com.bb8log.raspberrypi.adafruitmotorhat.motor.MotorCommand;

/**
 * Created by Florian on 27/02/2016.
 */
public class StepperMotor extends AbstractMotor {

    private static int MICROSTEPS = 8;
    private static int[] MICROSTEP_CURVE = {0, 50, 98, 142, 180, 212, 236, 250, 255};

    private int revsteps;
    private int msecPerStep = 100; // milli seconds per steps
    private int steppingCounter = 0;
    private int currentstep = 0;

    private StepperMotorInfos infos;

    public StepperMotor(AdafruitMotorHat controller, int num) {
        this(controller, num, 200);
    }

    public StepperMotor(AdafruitMotorHat controller, int num, int steps) {
        super(controller, num);
        this.revsteps = steps;
        this.infos = StepperMotorInfos.getByNum(num - 1);
    }

    public void setSpeed(int rpm) {
        msecPerStep = 60000 / (revsteps * rpm);
        steppingCounter = 0;
    }

    public int doOneStep(MotorCommand command, StepStyle stepsStyle) throws MotorException {
        int pwm_a = 255;
        int pwm_b = 255;

        // first determine what sort of stepping procedure we 're up to
        if (stepsStyle == StepStyle.SINGLE) {
            if ((currentstep / (MICROSTEPS / 2)) % 2 == 0) {
                // we 're at an odd step, weird
                if (command == MotorCommand.FORWARD) {
                    currentstep += MICROSTEPS / 2;
                } else {
                    currentstep -= MICROSTEPS / 2;
                }
            } else {
                // go to next even step
                if (command == MotorCommand.FORWARD) {
                    currentstep += MICROSTEPS;
                } else {
                    currentstep -= MICROSTEPS;
                }
            }
        }
        if (stepsStyle == StepStyle.DOUBLE) {
            if (currentstep / (MICROSTEPS / 2) % 2 != 0) {
                // we 're at an even step, weird
                if (command == MotorCommand.FORWARD) {
                    currentstep += MICROSTEPS / 2;
                } else {
                    currentstep -= MICROSTEPS / 2;
                }
            } else {
                // go to next odd step
                if (command == MotorCommand.FORWARD) {
                    currentstep += MICROSTEPS;
                } else {
                    currentstep -= MICROSTEPS;
                }
            }
        }
        if (stepsStyle == StepStyle.INTERLEAVE) {
            if (command == MotorCommand.FORWARD) {
                currentstep += MICROSTEPS / 2;
            } else {
                currentstep -= MICROSTEPS / 2;
            }
        }

        if (stepsStyle == StepStyle.MICROSTEP) {
            if (command == MotorCommand.FORWARD) {
                currentstep += 1;
            } else {
                currentstep -= 1;
            }

            // go to next 'step' and wrap around
            currentstep += MICROSTEPS * 4;
            currentstep %= MICROSTEPS * 4;

            pwm_a = pwm_b = 0;
            if (currentstep >= 0 && currentstep < MICROSTEPS) {
                pwm_a = MICROSTEP_CURVE[MICROSTEPS - currentstep];
                pwm_b = MICROSTEP_CURVE[currentstep];
            } else if ((currentstep >= MICROSTEPS) && (currentstep < MICROSTEPS * 2)) {
                pwm_a = MICROSTEP_CURVE[currentstep - MICROSTEPS];
                pwm_b = MICROSTEP_CURVE[MICROSTEPS * 2 - currentstep];
            } else if ((currentstep >= MICROSTEPS * 2) && (currentstep < MICROSTEPS * 3)) {
                pwm_a = MICROSTEP_CURVE[MICROSTEPS * 3 - currentstep];
                pwm_b = MICROSTEP_CURVE[currentstep - MICROSTEPS * 2];
            } else if ((currentstep >= MICROSTEPS * 3) && (currentstep < MICROSTEPS * 4)) {
                pwm_a = MICROSTEP_CURVE[currentstep - MICROSTEPS * 3];
                pwm_b = MICROSTEP_CURVE[MICROSTEPS * 4 - currentstep];
            }
        }

        // go to next 'step' and wrap around
        currentstep += MICROSTEPS * 4;
        currentstep %= MICROSTEPS * 4;

        // only really used for microstepping, otherwise always on!
        getController().getPwm().setPWM(infos.pwma, 0, pwm_a * 16);
        getController().getPwm().setPWM(infos.pwmb, 0, pwm_b * 16);

        // set up coil energizing!
        int[] coils = {0, 0, 0, 0};

        if (stepsStyle == StepStyle.MICROSTEP) {
            if ((currentstep >= 0) && (currentstep < MICROSTEPS)) {
                coils[0] = 1;
                coils[1] = 1;
                coils[2] = 0;
                coils[3] = 0;
            } else if ((currentstep >= MICROSTEPS) && (currentstep < MICROSTEPS * 2)) {
                coils[0] = 0;
                coils[1] = 1;
                coils[2] = 1;
                coils[3] = 0;
            } else if ((currentstep >= MICROSTEPS * 2) && (currentstep < MICROSTEPS * 3)) {
                coils[0] = 0;
                coils[1] = 0;
                coils[2] = 1;
                coils[3] = 1;
            } else if ((currentstep >= MICROSTEPS * 3) && (currentstep < MICROSTEPS * 4)) {
                coils[0] = 1;
                coils[1] = 0;
                coils[2] = 0;
                coils[3] = 1;
            } else {
                int[][] step2coils = {{1, 0, 0, 0},
                        {1, 1, 0, 0},
                        {0, 1, 0, 0},
                        {0, 1, 1, 0},
                        {0, 0, 1, 0},
                        {0, 0, 1, 1},
                        {0, 0, 0, 1},
                        {1, 0, 0, 1}};
                coils = step2coils[currentstep / (MICROSTEPS / 2)];
            }
        }
        // print "coils state = " + str(coils)
        getController().setPinValue(infos.ain2, coils[0]);
        getController().setPinValue(infos.bin1, coils[1]);
        getController().setPinValue(infos.ain1, coils[2]);
        getController().setPinValue(infos.bin2, coils[3]);

        return currentstep;
    }

    public void doSteps(int steps, MotorCommand command, StepStyle stepsStyle) throws InterruptedException, MotorException {
        int s_per_s = this.msecPerStep;
        int latestStep = 0;

        if (stepsStyle == StepStyle.INTERLEAVE) {
            s_per_s = s_per_s / 2000;
        }
        if (stepsStyle == StepStyle.MICROSTEP) {
            s_per_s /= this.MICROSTEPS;
            steps *= this.MICROSTEPS;
        }

        System.out.println(s_per_s + " milli sec per step");

        for (int i = 0; i < steps; i++) {
            latestStep = doOneStep(command, stepsStyle);
            Thread.sleep(s_per_s);
        }

        if (stepsStyle == StepStyle.MICROSTEP) {
            // #this is an edge case,if we are in between full steps, lets just keep going
            // #so we end on a full step
            while (latestStep != 0 && latestStep != MICROSTEPS) {
                latestStep = doOneStep(command, stepsStyle);
                Thread.sleep(s_per_s);
            }
        }
    }
}
