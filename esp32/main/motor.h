#ifndef DRIVE_MOTOR_H_
#define DRIVE_MOTOR_H_

#include "esp_err.h"
#include "driver/ledc.h"
#include "driver/gpio.h"

/**
 * @brief Configuration of drive motor
 * 
 */
typedef struct {
    uint8_t gpio_forward; 
    ledc_channel_t channel_forward;
    uint8_t gpio_reverse; 
    ledc_channel_t channel_reverse;
    ledc_mode_t speed_mode;
    ledc_timer_t timer;
    ledc_timer_bit_t resolution;
} motor_config_t;

/**
 * @brief Initialize ledc to control the drive motor
 * 
 * @param config Pointer of drive motor configure struct
 *
 * @return
 *     - ESP_OK Success
 *     - ESP_ERR_INVALID_ARG Parameter error
 *     - ESP_FAIL Configure ledc failed
 */
esp_err_t motor_init(const motor_config_t *config);


/**
 * @brief Set the drive motor to move forward
 * 
 * @param int32_t The speed
 * 
 * @return
 *     - ESP_OK Success
 */
void drive_motor(int32_t control);


#endif