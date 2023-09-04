#ifndef SOLAR_PANEL_H_
#define SOLAR_PANEL_H_

#include "analog_sensor.h"

/**
 * @brief Initiation Solar panel supervision
 *
 * @param waitingTime
 * @param panelSolarVoltageSensor
 * @return esp_err_t
 */
esp_err_t solarPanel(int waitingTime, analog_sensor_t *panelSolar);

/**
 * @brief Get the voltage from painel
 *
 * @return float
 */
float getReadings();

#endif