package com.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
@TableName("io_records")
@Data
public class io_Records {
    @TableId(type = IdType.AUTO)
    private int ioId;
    private String batchId;
    private String type;
    private String Time;
    private int operatorId;
    private String operatorName;
}
