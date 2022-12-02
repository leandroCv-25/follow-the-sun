
#include <stdio.h>
#include <string.h>

#include <freertos/FreeRTOS.h>
#include <freertos/task.h>

#include "esp_system.h"
#include "esp_utils.h"

#include <esp_wifi.h>
#include "driver/uart.h"

#include "solar_panel.h"
#include "control.h"
#include "comunication.h"
#include "tasks_common.h"

#define CONFIG_UART_TX_IO UART_PIN_NO_CHANGE
#define CONFIG_UART_RX_IO UART_PIN_NO_CHANGE

static const char * TAG = "Comunication"; 

void sendData(float voltage, float error, float angle)
{
    uint8_t  eth_mac[6];
    esp_wifi_get_mac(WIFI_IF_STA, eth_mac);
    printf("{\"mac\":\""MACSTR"\",\"angle\":%f,\"voltage\":%f,\"error\":%f}\n", MAC2STR(eth_mac), angle, voltage, error);
}

static void comunication_task(void *pvParameters)
{
    size_t size = 0;
    uint8_t *data = ESP_CALLOC(1, 255);

    vTaskDelay(250 / portTICK_PERIOD_MS);
    printf("\n");

    while (true)
    {
        int type;
        int autoControl;
        float ki;
        float kp;
        float kd;

        size = uart_read_bytes(UART_NUM_0, data, 255, pdMS_TO_TICKS(10));
        ESP_ERROR_CONTINUE(size <= 0, "");

        sscanf((char*)data, "{\"type\":%d,\"ki\":%f,\"kd\":%f,\"kp\":%f,\"auto\":%d}", &type, &ki, &kd, &kp, &autoControl);

        //printf("%s",data);

        if (type == 0)
        {
            setAuto(autoControl);
        }
        else if (type == 1)
        {
            setControllerGainProportion(kp);
            setControllerGainIntegration(ki);
            setControllerGainDerived(kd);
        }
        else if (type == 2)
        {
            findBestSpot();
        }

        memset(data, 0, 255);
    }

    // ESP_LOGI(TAG, "Uart handle task is exit");

    ESP_FREE(data);
    vTaskDelete(NULL);
}

void comunication_init(uart_config_t *uart_config)
{
    ESP_ERROR_CHECK(uart_param_config(UART_NUM_0, uart_config));
    ESP_ERROR_CHECK(uart_set_pin(UART_NUM_0, CONFIG_UART_TX_IO, CONFIG_UART_RX_IO, UART_PIN_NO_CHANGE, UART_PIN_NO_CHANGE));
    ESP_ERROR_CHECK(uart_driver_install(UART_NUM_0, 8 * 255, 8 * 255, 0, NULL, 0));

    xTaskCreatePinnedToCore(&comunication_task, "comunication_task", COMUNICATION_TASK_STACK_SIZE, NULL, COMUNICATION_TASK_PRIORITY, NULL, COMUNICATION_TASK_CORE_ID);
}