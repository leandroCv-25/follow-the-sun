#ifndef CONTROL_H_
#define CONTROL_H_

#include "freertos/FreeRTOS.h"
#include "freertos/task.h"

/**
 * @brief Set the Controller Gain Derived object
 *
 * @param k
 */
void setControllerGainDerived(double k);

/**
 * @brief Set the Controller Gain Proportion object
 *
 * @param k
 */
void setControllerGainProportion(double k);

/**
 * @brief Set the Controller Gain Integration object
 *
 * @param k
 */
void setControllerGainIntegration(double k);

/**
 * @brief Set the Angle object
 *
 * @param angle
 */
void setAngle(float angle);

/**
 * @brief Get the Angle object
 *
 * @return float
 */
float getAngle();

/**
 * @brief Get the Error object
 *
 * @return float
 */
float getError();

/**
 * @brief Configuration control Init
 * 
 * @param motorDC 
 * @param lightSensor 
 * @param angleSensor 
 */
esp_err_t control(motor_drive_t motorDC, analog_sensor_t lightSensor, analog_sensor_t angleSensor);
#endif