package com.manage.entity;

import lombok.Data;

@Data
public class Batches {
    private int batchId;
    private String batchType;
    private int operatorId;
    private int reviewerId;
    private String opTime;
    private String reviewTime;
    private int isAprroved;
    private String comment;
}
