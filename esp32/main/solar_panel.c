#include <stdio.h>
#include <stdlib.h>
#include "freertos/FreeRTOS.h"
#include "freertos/task.h"
#include "driver/gpio.h"
#include "driver/adc.h"
#include "esp_adc_cal.h"

#include "solar_panel.h"
#include "control.h"
#include "comunication.h"
#include "tasks_common.h"

#define DEFAULT_VREF 1100 // Use adc2_vref_to_gpio() to obtain a better estimate
#define NO_OF_SAMPLES 64  // Multisampling

static esp_adc_cal_characteristics_t *adc_chars;

static adc_channel_t channel;
static adc_bits_width_t width;

static adc_atten_t atten;
static adc_unit_t unit;

static int autoControl = 0;

static uint32_t waitingTime = 60;

uint32_t last_adc_reading = 0;

static uint32_t read_panel()
{
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

    return adc_reading;
}

static void panel_task(void *pvParameters)
{
    uint8_t count = 0;
    uint32_t adc_reading = 0;

    while (true)
    {
        count++;

        adc_reading += read_panel() / waitingTime;

        if (count == waitingTime)
        {
            if ((last_adc_reading < adc_reading * 0.9 || last_adc_reading > adc_reading * 1.1) && autoControl)
            {
                findBestSpot();
            }

            sendData((float) adc_reading / 3300, getError(), getAngle());

            last_adc_reading = adc_reading;
            adc_reading = 0;
            count = 0;
        }

        vTaskDelay(100 / portTICK_PERIOD_MS);
    }
}

void setAuto(int value)
{
    autoControl = value;
}

void findBestSpot()
{
    setAngle(0);
    uint32_t adc_reading = read_panel();
    uint32_t max_adc_reading = adc_reading;
    float bestAngle = 0;
    vTaskDelay(5000 / portTICK_PERIOD_MS);
    for (float i = 1; i <= 300; i += 0.5)
    {
        setAngle(i);
        vTaskDelay(250 / portTICK_PERIOD_MS);
        adc_reading = read_panel();
        if (adc_reading > max_adc_reading)
        {
            max_adc_reading = adc_reading;
            bestAngle = i;
        }
        sendData((float) adc_reading / 3300, getError(), getAngle());
    }

    setAngle(bestAngle);
}

void solar_panel_init(adc_channel_t adcChannel, adc_bits_width_t adcWidth, adc_atten_t adcAtten, adc_unit_t adcUnit)
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

    xTaskCreatePinnedToCore(&panel_task, "panel_task", PANEL_TASK_STACK_SIZE, NULL, PANEL_TASK_PRIORITY, NULL, PANEL_TASK_CORE_ID);
}