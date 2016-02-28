package com.bb8log.raspberrypi.motorhat.motor;

/**
 * Created by Florian on 27/02/2016.
 */
public enum DCMotorInfos {
    // num, pwm, in1, in2
    DC_MOTOR_0(0, 8, 10, 9),
    DC_MOTOR_1(1, 13, 11, 12),
    DC_MOTOR_2(2, 2, 4, 3),
    DC_MOTOR_3(3, 7, 5, 6);

    public int num;
    public int pwm;
    public int in1;
    public int in2;

    DCMotorInfos(int num, int pwm, int in1, int in2) {
        this.num = num;
        this.pwm = pwm;
        this.in1 = in1;
        this.in2 = in2;
    }

    public static DCMotorInfos getByNum(int num) {
        for (DCMotorInfos dcMotorInfos : values()) {
            if (dcMotorInfos.num == num)
                return dcMotorInfos;
        }
        return null;
    }
}
