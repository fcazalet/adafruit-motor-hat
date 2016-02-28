package com.bb8log.raspberrypi.motorhat.adafruit;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

/**
 * Created by Florian on 27/02/2016.
 * traduit du script python : https://github.com/adafruit/Adafruit-Motor-HAT-Python-Library/blob/master/Adafruit_MotorHAT/Adafruit_PWM_Servo_Driver.py
 */
public class PWM {
    byte __MODE1 = (byte) 0x00;
    byte __MODE2 = (byte) 0x01;
    byte __SUBADR1 = (byte) 0x02;
    byte __SUBADR2 = (byte) 0x03;
    byte __SUBADR3 = (byte) 0x04;
    byte __PRESCALE = (byte) 0xFE;
    byte __LED0_ON_L = (byte) 0x06;
    byte __LED0_ON_H = (byte) 0x07;
    byte __LED0_OFF_L = (byte) 0x08;
    byte __LED0_OFF_H = (byte) 0x09;
    int __ALL_LED_ON_L = (byte) 0xFA;
    byte __ALL_LED_ON_H = (byte) 0xFB;
    byte __ALL_LED_OFF_L = (byte) 0xFC;
    byte __ALL_LED_OFF_H = (byte) 0xFD;

    //    Bits
    byte __RESTART = (byte) 0x80;
    byte __SLEEP = (byte) 0x10;
    byte __ALLCALL = (byte) 0x01;
    byte __INVRT = (byte) 0x10;
    byte __OUTDRV = (byte) 0x04;

    private I2CDevice i2CDevice;
    private int address = 0x40;
    private boolean debug = false;

    public PWM(I2CDevice i2CDevice, int address, boolean debug) throws IOException {
        this.i2CDevice = i2CDevice;
        this.address = address;
        this.debug = debug;
    }

    public void setPWM(int channel, int on, int off) {
        write8(__LED0_ON_L + 4 * channel, on & 0xFF);
        write8(__LED0_ON_H + 4 * channel, on >> 8);
        write8(__LED0_OFF_L + 4 * channel, off & 0xFF);
        write8(__LED0_OFF_H + 4 * channel, off >> 8);
    }

    public void setAllPWM(int on, int off) {
        // Sets a all PWM channels
        write8(__ALL_LED_ON_L, on & 0xFF);
        write8(__ALL_LED_ON_H, on >> 8);
        write8(__ALL_LED_OFF_L, off & 0xFF);
        write8(__ALL_LED_OFF_H, off >> 8);
    }

    public void setPWMFreq(int freq) throws IOException {
        // Sets the PWM frequency
        float prescaleval = 25000000.0f; // 25 MHz
        prescaleval /= 4096.0f; // 12-bit
        prescaleval /= (float) freq;
        prescaleval -= 1.0f;
        if (debug) {
            System.out.println("Setting PWM frequency to " + freq + " Hz");
            System.out.println("Estimated pre-scale: " + prescaleval);
        }
        double prescale = Math.floor(prescaleval + 0.5);
        if (debug) {
            System.out.println("Final pre-scale: " + prescale);
        }
        int oldmode = readU8(__MODE1);
        int newmode = ((oldmode & 0x7F) | 0x10); // #sleep
        write8(__MODE1, newmode);//#go to sleep
        write8(__PRESCALE, (int) Math.floor(prescale));
        write8(__MODE1, oldmode);
        try {
            wait(5); // wait 5 ms
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        write8(__MODE1, oldmode | 0x80);
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public void write8(int register, int value) {
        try {
            i2CDevice.write(register, (byte) value);
            if (debug) {
                System.out.println("I2C: Wrote 0x" + value + "02X to register 0x" + register + "02X");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int readU8(int register) throws IOException {
        // Read an unsigned byte from the I2C device
        int result = i2CDevice.read(register);
        if (debug) {
            System.out.println("I2C: Device 0x" + address + "02X returned 0x" + (result & 0xFF) + "02X from reg 0x" + register + "02X");
        }
        return result;
    }
}

