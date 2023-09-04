#include <math.h>

#include "freertos/FreeRTOS.h"
#include "freertos/task.h"

#include "esp_log.h"

#include "tasks_common.h"
#include "analog_sensor.h"
#include "solar_panel.h"

static const char *tag = "Solar Painel";
float last_reading = 0;

int waitingTime;
analog_sensor_t *panelSolarVoltageSensor;

static void panel_task(void *pvParameters)
{
    uint8_t count = 0;
    int adc_reading = 0;

    while (true)
    {
        count++;

        adc_reading += (read_sensor(panelSolarVoltageSensor) / waitingTime);

        if (count == waitingTime)
        {

            last_reading = adc_reading;
            adc_reading = 0;
            count = 0;
        }

        vTaskDelay(1000 / portTICK_PERIOD_MS);
    }
}

esp_err_t solarPanel(int waitingTime, analog_sensor_t *panelSolar)
{
    panelSolarVoltageSensor = panelSolar;
    waitingTime = waitingTime;

     xTaskCreatePinnedToCore(&panel_task, "panel_task", PANEL_TASK_STACK_SIZE, NULL, PANEL_TASK_PRIORITY, NULL, PANEL_TASK_CORE_ID);

    return ESP_OK;
}


float getReadings()
{
    return last_reading;
}