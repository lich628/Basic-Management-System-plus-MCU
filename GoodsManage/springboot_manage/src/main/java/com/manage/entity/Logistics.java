package com.manage.entity;

import lombok.Data;

@Data
public class Logistics {
    private int id;
    private String batchId;
    private String logisticsStatus;
    private String logisticsInfo;
    private String destination;
}
