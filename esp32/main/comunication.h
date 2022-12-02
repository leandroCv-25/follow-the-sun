#ifndef COMUNICATION_H_
#define COMUNICATION_H_

#include "driver/uart.h"

void comunication_init(uart_config_t*uart_config);

void sendData(float voltage, float error, float angle);

#endif