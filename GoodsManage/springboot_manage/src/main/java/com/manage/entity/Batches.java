package com.manage.entity;

import lombok.Data;

@Data
public class Batches {
    private int batchId;
    private int operatorId;
    private String opTime;
    private String opType;
}
