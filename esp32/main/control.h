#ifndef CONTROLPID_H_
#define CONTROLPID_H_


#include "esp_adc_cal.h"

void setControllerGainDerived(double k);

void setControllerGainProportion(double k);

void setControllerGainIntegration(double k);

float getAngle();

float getError();

void setAngle(float angle);

void config_control(adc_channel_t adcChannel,adc_bits_width_t adcWidth,adc_atten_t adcAtten,adc_unit_t adcUnit);

#endif