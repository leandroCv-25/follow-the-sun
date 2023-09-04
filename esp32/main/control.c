#include <math.h>

#include "freertos/FreeRTOS.h"
#include "freertos/task.h"

#include "driver/gpio.h"

#include "esp_log.h"
#include "esp_err.h"

#include "tasks_common.h"
#include "analog_sensor.h"
#include "motor_dc.h"
#include "control.h"

static const char *tag = "Control";

motor_drive_t motor;
analog_sensor_t lightSensor;
analog_sensor_t angleSensor;

float angle;
float error;
float eIntegral;
int32_t e;
int32_t e0;
int32_t controlVar;
float objetiveAngle;

double kp = 2;
double kd = 0;
double ki = 0;

/**
 * @brief adcRawToAngle
 * Converts the reading to degrees
 * @param reading
 * @return float
 */
float adcRawToAngle(int reading)
{
    return (float)reading * 300.0 / getSensorWidth(&angleSensor);
}

/**
 * @brief angleToAdcRaw
 * Converts the degrees to Adc
 * @param angle
 * @return int
 */
int angleToAdcRaw(float angle)
{
    return (int)(angle * getSensorWidth(&angleSensor) / 300);
}

/**
 * @brief ControlAngle
 * Control the device via angle
 */
void controlAngle()
{

    int setPoint = angleToAdcRaw(objetiveAngle);

    int adc_reading = read_sensor(&angleSensor);

    e = setPoint - adc_reading;
    eIntegral += (e + e0) / 2;
    controlVar = (int32_t)(kp * e + kd * (e - e0) + ki * eIntegral);
    e0 = e;

    error = abs(e);

    angle = adcRawToAngle(adc_reading);

    driveMotor(&motor, controlVar);
}

void resetControl()
{
    eIntegral = 0;
    e = 0;
    e0 = 0;
    controlVar = 0;
}

void control_task(void *pvParameters)
{

    resetControl();
    int count = 0;
    while (true)
    {
        int adc_reading = read_sensor(&lightSensor);

        error = (adc_reading - pow(2, getSensorWidth(&lightSensor) - 1));

        e = error;
        eIntegral += (e + e0) / 2;
        controlVar = (int32_t)(kp * e + kd * (e - e0) + ki * eIntegral);
        e0 = e;

        driveMotor(&motor, controlVar);

        vTaskDelay(50 / portTICK_PERIOD_MS);
        count++;
        if (count % 10 == 0)
        {
            ESP_LOGI(tag, "Error: %f", error);
            ESP_LOGI(tag, "Control: %li\n", controlVar);
            count = 0;
        }
    }
}

void setControllerGainDerived(double k)
{
    kd = k;
}

void setControllerGainProportion(double k)
{
    kp = k;
}

void setControllerGainIntegration(double k)
{
    ki = k;
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

esp_err_t control(motor_drive_t motorDC, analog_sensor_t light, analog_sensor_t angle)
{
    motor = motorDC;
    lightSensor = light;
    angleSensor = angle;

    xTaskCreatePinnedToCore(&control_task, "control_task", CONTROL_TASK_STACK_SIZE, NULL, CONTROL_TASK_PRIORITY, NULL, CONTROL_TASK_CORE_ID);

    return ESP_OK;
}