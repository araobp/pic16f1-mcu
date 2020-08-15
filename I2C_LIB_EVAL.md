# I2C library evaluation (PIC1825 with MPU9250)

(Work in progress)

## Background

Last year, Microchip released a new I2C library that is generated by MCC automatically. I have not swiched from the old verion to the new version yet.

In this project, I evaluate the new library with InvenSense (TDK) MPU9250 9-axis sensor that can be purchased from Amazon.com at a very reasonable price.

```
 [InvenSense MPU-9255 (HiLetgo MPU9250 module)]--I2C--[PIC16F1825]--USB serial--[TeraTerm on Win10 PC]

```

I also develop an Android app to monitor data from MPU9250.
```
 [InvenSense MPU-9255 (HiLetgo MPU9250 module)]--I2C--[PIC16F1825]--USB serial--[Android app]

```

## InvenSense (TDK) MPU-9250/9255

The module I purchased from Amazon is MPU-9255. The module includes an accelerometer, a gyroscope and a mangetometer from Asahi Kasei.

## Schematic

[Schematic](./kicad/i2c_lib_eval/i2c_lib_eval.pdf)

## Code 

I use one I2C bus to access all the sensors in the module.

==> [Code for PIC16F1825](./src/pic16f1825/i2c_lib_eval.X)

==> [Code for Android](./android/mpu9250)