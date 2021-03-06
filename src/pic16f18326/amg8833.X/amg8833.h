/* 
 * File:   amg8833.h
 *
 * Created on January 11, 2019, 9:44 PM
 */

#ifndef AMG8833_H
#define	AMG8833_H

#ifdef	__cplusplus
extern "C" {
#endif

#include <stdbool.h>
#include <stdint.h>
#include "i2c_util.h"

// Infrared array sensor "Panasonic AMG8833"
#define AMG8833_DEV_ADDR 0x68
#define AMG8833_T01L_ADDR 0x80
#define AMG8833_TTHL_ADDR 0x0E
#define AMG8833_1F_ADDR 0x1F
#define AMG8833_AVE_ADDR 0x07
#define AMG8833_THERMISTOR_LENGTH 2U    
#define AMG8833_PIXELS_LENGTH 128U
#define AMG8833_PIXELS_LENGTH_HALF 64U

// Absolute value of diff larger than this value is regarded as moving.
#define PEAK_COUNT_THRESHOLD 4

// Resolution to identify an object
#define OBJECT_RESOLUTION 2
    
// Motion count on a specific row: this value is an index of the row. 
#define SCAN_ROW 4
    
typedef struct {
    uint8_t thermistor[2];
    uint8_t pixels[AMG8833_PIXELS_LENGTH];
    uint8_t pixels_prev[AMG8833_PIXELS_LENGTH / 2];
    int8_t diff[AMG8833_PIXELS_LENGTH / 2];
    int8_t line[8];
} amg8833_instance;

void set_moving_average(amg8833_instance *A, bool enable);

void update_thermistor(amg8833_instance *A);

void update_pixels(amg8833_instance *A);

bool update_diff(amg8833_instance *A, bool flag);

void update_diff_motion(amg8833_instance *A);

bool update_line(amg8833_instance *A);

void calibrate_threshold(int v);

#ifdef	__cplusplus
}
#endif

#endif	/* AMG8833_H */

