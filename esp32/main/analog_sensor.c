#include "esp_adc/adc_oneshot.h"
#include "esp_log.h"

#include "analog_sensor.h"

esp_err_t analogSensor(analog_sensor_t* sensor){
    adc_oneshot_chan_cfg_t config;

    config.bitwidth = sensor->adcWidth;
    config.atten = sensor->adcAtten;

    return adc_oneshot_config_channel(sensor->adcUnit, sensor->adcChannel, &config);
}

int getSensorWidth(analog_sensor_t* analogSensor)
{
    return analogSensor->adcWidth;
}

int read_sensor(analog_sensor_t* analogSensor)
{
    int adc_reading = 0;
    // Multisampling
    for (int i = 0; i < analogSensor->numberSamples; i++)
    {
        int adc = 0;
        adc_oneshot_read(analogSensor->adcUnit, analogSensor->adcChannel, &adc);
        adc_reading += adc;
    }
    
    adc_reading /= (analogSensor->numberSamples);

    return adc_reading;
}