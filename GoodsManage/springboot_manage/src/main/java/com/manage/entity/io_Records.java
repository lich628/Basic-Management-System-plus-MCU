package com.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class io_Records {
    @TableId(type = IdType.AUTO)
    private int ioId;
    private int batchId;
    private int operatorId;
    private String operationType;
    private String operationTime;
}
