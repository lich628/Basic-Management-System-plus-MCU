package com.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

@Data
public class Batches {
    @TableId(type = IdType.AUTO)
    private String batchId;
    private String batchType;
    private int batchStatus;
    private int operatorId;
    private int reviewerId;
    private String opTime;
    private String reviewTime;
    private String opComment;
    private String viewerComment;
    private int isClosed;
    private String cardUid;
    @TableField(exist = false)
    private List<batchGoodsDetail> batchGoodsDetail;
}
