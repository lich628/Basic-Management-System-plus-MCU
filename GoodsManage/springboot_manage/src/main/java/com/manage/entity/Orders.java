package com.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Orders {
    @TableId(type = IdType.AUTO)
    private int orderId;
    private String batchId;
    private int quantity;
    private String status;
    private String operatorId;
    private String auditorId;
}
