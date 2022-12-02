#ifndef SOLAR_PANEL_H_
#define SOLAR_PANEL_H_

#include "esp_adc_cal.h"

void solar_panel_init(adc_channel_t adcChannel, adc_bits_width_t adcWidth, adc_atten_t adcAtten, adc_unit_t adcUnit);

void setAuto(int value);

void findBestSpot();

#endif