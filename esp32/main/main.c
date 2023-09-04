#include "driver/ledc.h"
#include "driver/gpio.h"
#include "esp_adc/adc_oneshot.h"

#include "nvs_flash.h"
#include "esp_log.h"

#include "motor_dc.h"
#include "analog_sensor.h"
#include "solar_panel.h"
#include "control.h"

static const char TAG[] = "main";

adc_oneshot_unit_init_cfg_t adc_unit_config = {
    .unit_id = ADC_UNIT_1,
    .ulp_mode = ADC_ULP_MODE_DISABLE,
};

void app_main(void)
{

    // INITIALISE NVS
    esp_err_t ret = nvs_flash_init();
    if (ret == ESP_ERR_NVS_NO_FREE_PAGES || ret == ESP_ERR_NVS_NEW_VERSION_FOUND)
    {
        ESP_ERROR_CHECK(nvs_flash_erase());
        ret = nvs_flash_init();
    }

    ESP_LOGI(TAG, "ESP STARTING!");

    adc_oneshot_unit_handle_t adc1;
    ESP_ERROR_CHECK(adc_oneshot_new_unit(&adc_unit_config, &adc1));

    analog_sensor_t lightSensor = {
        .adcChannel = ADC_CHANNEL_6,
        .adcWidth = ADC_BITWIDTH_12,
        .adcAtten = ADC_ATTEN_DB_11,
        .numberSamples = 64,
        .adcUnit = adc1,
    };
    ESP_LOGI(TAG, "Light sensor STARTING!");
    ESP_ERROR_CHECK(analogSensor(&lightSensor));

    analog_sensor_t angleSensor = {
        .adcChannel = ADC_CHANNEL_7,
        .adcWidth = ADC_BITWIDTH_12,
        .adcAtten = ADC_ATTEN_DB_11,
        .numberSamples = 64,
        .adcUnit = adc1,
    };

    ESP_LOGI(TAG, "Angle sensor STARTING!");
    ESP_ERROR_CHECK(analogSensor(&angleSensor));

    motor_drive_t motor = {
        .gpio_forward = GPIO_NUM_18,
        .channel_forward = LEDC_CHANNEL_0,
        .gpio_reverse = GPIO_NUM_19,
        .channel_reverse = LEDC_CHANNEL_1,
        .speed_mode = LEDC_HIGH_SPEED_MODE,
        .timer = LEDC_TIMER_1,
        .resolution = LEDC_TIMER_10_BIT,
        .m_full_duty = (1 << LEDC_TIMER_10_BIT) - 1,
    };

    ESP_LOGI(TAG, "driver motor STARTING!");
    ESP_ERROR_CHECK(motorDC(&motor));

    control(motor,lightSensor,angleSensor);


    //  analog_sensor_t voltageSensorPanel = {
    //     .adcChannel = ADC_CHANNEL_4,
    //     .adcWidth = ADC_BITWIDTH_12,
    //     .adcAtten = ADC_ATTEN_DB_11,
    //     .numberSamples = 64,
    //     .adcUnit = adc1,
    // };

    // ESP_LOGI(TAG, "Angle sensor STARTING!");
    // ESP_ERROR_CHECK(analogSensor(&voltageSensorPanel));
    // solarPanel(30,&voltageSensorPanel);
}
