#ifndef ANALOG_SENSOR_H_
#define ANALOG_SENSOR_H_
#include "esp_adc/adc_oneshot.h"

typedef struct
{
    adc_channel_t adcChannel;
    adc_bitwidth_t adcWidth;
    adc_atten_t adcAtten;
    int numberSamples;
    adc_oneshot_unit_handle_t adcUnit;
} analog_sensor_t;

/**
 * @brief analogSensor
 * Configuration and initiation ADC port
 * @param analogSensor 
 * @return esp_err_t 
 */
esp_err_t analogSensor(analog_sensor_t* analogSensor);

/**
 * @brief Get the Width Analog sensor
 * 
 * @param analogSensor 
 * @return int 
 */
int getSensorWidth(analog_sensor_t* analogSensor);

/**
 * @brief Read the value in the sensor uses the configuration for that
 * 
 * @param analogSensor 
 * @return int 
 */
int read_sensor(analog_sensor_t* analogSensor);

#endif