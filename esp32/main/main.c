
#include "solar_panel.h"
#include "control.h"
#include "motor.h"
#include "comunication.h"
#include "driver/ledc.h"
#include "driver/gpio.h"

#include "driver/uart.h"

static motor_config_t motorConfig = {
    .gpio_forward = GPIO_NUM_18,
    .channel_forward = LEDC_CHANNEL_0,
    .gpio_reverse = GPIO_NUM_19,
    .channel_reverse = LEDC_CHANNEL_1,
    .speed_mode = LEDC_HIGH_SPEED_MODE,
    .timer = LEDC_TIMER_1,
    .resolution = LEDC_TIMER_10_BIT,
};

uart_config_t uart_config = {
        .baud_rate = 115200,
        .data_bits = UART_DATA_8_BITS,
        .parity    = UART_PARITY_DISABLE,
        .stop_bits = UART_STOP_BITS_1,
        .flow_ctrl = UART_HW_FLOWCTRL_DISABLE
    };

void app_main(void)
{
   motor_init(&motorConfig);

   comunication_init(&uart_config);

   config_control(ADC_CHANNEL_7,ADC_WIDTH_BIT_12,ADC_ATTEN_DB_11,ADC_UNIT_1);

   solar_panel_init(ADC_CHANNEL_6,ADC_WIDTH_BIT_12,ADC_ATTEN_DB_11,ADC_UNIT_1);
}