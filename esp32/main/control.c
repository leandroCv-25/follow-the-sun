#include <stdio.h>
#include <stdlib.h>
#include "freertos/FreeRTOS.h"
#include "freertos/task.h"
#include "driver/gpio.h"
#include "driver/adc.h"
#include "esp_adc_cal.h"

#include "control.h"
#include "motor.h"
#include "tasks_common.h"

#define DEFAULT_VREF 1100 // Use adc2_vref_to_gpio() to obtain a better estimate
#define NO_OF_SAMPLES 64  // Multisampling

static esp_adc_cal_characteristics_t *adc_chars;

static adc_channel_t channel;
static adc_bits_width_t width;

static adc_atten_t atten;
static adc_unit_t unit;

float angle;
float error;
float objetiveAngle = 0;

uint32_t kp = 0;
uint32_t kd = 0;
uint32_t ki = 0;

static float adc_raw_to_angle(uint32_t reading)
{
    return (float)reading * 300.0 / 4096;
}

static uint32_t angle_to_adc_raw(float angle)
{
    return (uint32_t)angle * 4096 / 300;
}

void setControllerGainDerived(double k){
    kd = k;
}

void setControllerGainProportion(double k){
    kp = k;
}

void setControllerGainIntegration(double k){
    ki = k;
}

/**
 * Main task for the control task
 * @param pvParameters parameter which can be passed to the task
 */
static void control_task(void *pvParameters)
{
    int32_t eIntegral = 0;
    int32_t e = 0;
    int32_t e0 = 0;
    int32_t control = 0;

    error = 0;

    // Continuously sample ADC1
    while (true)
    {
        uint32_t setPoint = angle_to_adc_raw(objetiveAngle);

        uint32_t adc_reading = 0;

        // Multisampling
        for (int i = 0; i < NO_OF_SAMPLES; i++)
        {
            if (unit == ADC_UNIT_1)
            {
                adc_reading += adc1_get_raw((adc1_channel_t)channel);
            }
            else
            {
                int raw;
                adc2_get_raw((adc2_channel_t)channel, width, &raw);
                adc_reading += raw;
            }
        }
        adc_reading /= NO_OF_SAMPLES;

        e = setPoint - adc_reading;
        //printf("%d - %d = erro: %d\n", setPoint, adc_reading, e);
        eIntegral += (e + e0) / 2;
        control = kp * e + kd * (e - e0) + ki * eIntegral;
        //printf("Control: %d\n", control);
        e0 = e;

        error = adc_raw_to_angle(abs(e));

        ///printf("Control: %d\terro: %d\n", control, e);
        angle = adc_raw_to_angle(adc_reading);
        //printf("Raw: %d\tAngle: %f°\n", adc_reading, angle);

        drive_motor(control);

        vTaskDelay(50 / portTICK_PERIOD_MS);
    }
}

void setAngle(float angle)
{
    objetiveAngle = angle;
}

float getAngle()
{
    return angle;
}

float getError()
{
    return error;
}

void config_control(adc_channel_t adcChannel, adc_bits_width_t adcWidth, adc_atten_t adcAtten, adc_unit_t adcUnit)
{

    channel = adcChannel;
    width = adcWidth;

    atten = adcAtten;
    unit = adcUnit;

    // Configure ADC
    if (unit == ADC_UNIT_1)
    {
        adc1_config_width(width);
        adc1_config_channel_atten(channel, atten);
    }
    else
    {
        adc2_config_channel_atten((adc2_channel_t)channel, atten);
    }

    // Characterize ADC
    adc_chars = calloc(1, sizeof(esp_adc_cal_characteristics_t));
    esp_adc_cal_characterize(unit, atten, width, DEFAULT_VREF, adc_chars);

    xTaskCreatePinnedToCore(&control_task, "control_task", CONTROL_TASK_STACK_SIZE, NULL, CONTROL_TASK_PRIORITY, NULL, CONTROL_TASK_CORE_ID);
}