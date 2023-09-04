#include "esp_log.h"
#include "esp_err.h"
#include "driver/ledc.h"

#include "motor_dc.h"

static const char *tag = "drive motor";

static esp_err_t setDriveMotorDuty(ledc_mode_t speedMode, uint32_t duty, ledc_channel_t channel)
{
    esp_err_t ret = ledc_set_duty(speedMode, channel, duty);
    ret |= ledc_update_duty(speedMode, channel);
    // ESP_LOGI(TAG, "set duty %i on channel %i", duty, channel);
    return ret;
}

static uint32_t validateSpeed(motor_drive_t *m_cfg, uint32_t speed)
{
    if (speed > m_cfg->m_full_duty)
    {
        return m_cfg->m_full_duty;
    }
    return speed;
}

static void driveMotorForward(motor_drive_t *m_cfg, uint32_t speed)
{
    speed = validateSpeed(m_cfg,speed);

    esp_err_t ret = setDriveMotorDuty(m_cfg->speed_mode, 0, m_cfg->channel_reverse);
    ret |= setDriveMotorDuty(m_cfg->speed_mode, speed, m_cfg->channel_forward);

    if (ret != ESP_OK)
    {
        ESP_LOGE(tag, "forward is failing");
    }
}

static void driveMotorReverse(motor_drive_t *m_cfg, uint32_t speed)
{
    speed = validateSpeed(m_cfg,speed);

    esp_err_t ret = setDriveMotorDuty(m_cfg->speed_mode, 0, m_cfg->channel_forward);
    ret |= setDriveMotorDuty(m_cfg->speed_mode, speed, m_cfg->channel_reverse);

    if (ret != ESP_OK)
    {
        ESP_LOGE(tag, "Reverse is failing");
    }
}

void driveMotor(motor_drive_t *m_cfg, int32_t speed)
{
    
    if (speed > 0)
    {
        driveMotorForward(m_cfg, speed);
    }
    else if (speed < 0)
    {
        driveMotorReverse(m_cfg, -speed);
    }
}

esp_err_t motorDC(motor_drive_t *m_cfg)
{
    esp_err_t ret;
    if (m_cfg == NULL)
    {
        ESP_LOGE(tag, "Pointer of config is invalid");
        return ESP_ERR_INVALID_ARG;
    }

    ledc_timer_config_t ledc_timer = {
        .clk_cfg = LEDC_AUTO_CLK,
        .duty_resolution = m_cfg->resolution, // resolution of PWM duty
        .freq_hz = 5000,                      // frequency of PWM signal
        .speed_mode = m_cfg->speed_mode,      // timer mode
        .timer_num = m_cfg->timer             // timer index
    };
    ret = ledc_timer_config(&ledc_timer);

    if (ESP_OK != ret)
    {
        ESP_LOGE(tag, "ledc timer configuration failed");
        return ESP_FAIL;
    }

    ledc_channel_config_t ledc_chForward = {
        .intr_type = LEDC_INTR_DISABLE,
        .channel = m_cfg->channel_forward,
        .duty = 0,
        .gpio_num = m_cfg->gpio_forward,
        .speed_mode = m_cfg->speed_mode,
        .timer_sel = m_cfg->timer,
        .hpoint = 0};

    ret = ledc_channel_config(&ledc_chForward);

    if (ESP_OK != ret)
    {
        ESP_LOGE(tag, "ledc channel configuration failed");
        return ESP_FAIL;
    }

    ledc_channel_config_t ledc_chReverse = {
        .intr_type = LEDC_INTR_DISABLE,
        .channel = m_cfg->channel_reverse,
        .duty = 0,
        .gpio_num = m_cfg->gpio_reverse,
        .speed_mode = m_cfg->speed_mode,
        .timer_sel = m_cfg->timer,
        .hpoint = 0};

    ret = ledc_channel_config(&ledc_chReverse);

    if (ESP_OK != ret)
    {
        ESP_LOGE(tag, "ledc channel configuration failed");
        return ESP_FAIL;
    }

    return ESP_OK;
}
