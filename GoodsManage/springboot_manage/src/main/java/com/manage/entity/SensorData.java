package com.manage.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("env_records")
public class SensorData {
    private double Humidity;
    private double Temperature;
    private double SmokeSensor;
    private String cardUid;
}
