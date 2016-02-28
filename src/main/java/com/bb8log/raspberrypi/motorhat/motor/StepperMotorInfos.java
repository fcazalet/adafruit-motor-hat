package com.bb8log.raspberrypi.motorhat.motor;

/**
 * Created by Florian on 27/02/2016.
 */
public enum StepperMotorInfos {
    // num, pwm, in1, in2
    ST_MOTOR_0(0, 8, 10, 9, 13, 11, 12),
    ST_MOTOR_1(1, 2, 4, 3, 7, 5, 6);

    public int num;
    public int pwma;
    public int ain1;
    public int ain2;
    public int pwmb;
    public int bin1;
    public int bin2;

    StepperMotorInfos(int num, int pwma, int ain1, int ain2, int pwmb, int bin1, int bin2) {
        this.num = num;
        this.pwma = pwma;
        this.ain1 = ain1;
        this.ain2 = ain2;
        this.pwmb = pwmb;
        this.bin1 = bin1;
        this.bin2 = bin2;
    }

    public static StepperMotorInfos getByNum(int num) {
        for (StepperMotorInfos dcMotorInfos : values()) {
            if (dcMotorInfos.num == num)
                return dcMotorInfos;
        }
        return null;
    }
}
