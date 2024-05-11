package com.manage.entity;

import lombok.Data;

@Data
public class SensorData {
    private double humidity;
    private double temperature;
    private double smokeSensorValue;
    private String cardUid;
}
