package com.manage.entity;

import lombok.Data;

@Data
public class stagingArea {
    private int stagingId;
    private int goodsId;
    private int operatorId;
    private int quantity;
    private String opTime;
}
