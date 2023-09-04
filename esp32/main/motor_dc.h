#ifndef MOTOR_DC_H_
#define MOTOR_DC_H_

#include "esp_err.h"
#include "driver/ledc.h"

/**
 * @brief motor_drive_t
 * Configuration of driver motor DC
 */
typedef struct
{
    uint8_t gpio_forward;
    ledc_channel_t channel_forward;
    uint8_t gpio_reverse;
    ledc_channel_t channel_reverse;
    ledc_mode_t speed_mode;
    ledc_timer_t timer;
    ledc_timer_bit_t resolution;
    uint32_t m_full_duty;
} motor_drive_t;


/**
 * @brief motorDC
 *  Initialize ledc to control the drive Motor DC
 * @param m_cfg
 * @return esp_err_t
 */
esp_err_t motorDC(motor_drive_t *cfg);


/**
 * @brief Set the drive motor to move
 * 
 * @param m_cfg 
 * @param speed 
 */
void driveMotor(motor_drive_t *m_cfg, int32_t speed);

#endif