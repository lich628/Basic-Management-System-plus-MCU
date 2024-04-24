package com.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class Goods {
    @TableId(type = IdType.AUTO)
    private int goodsId;
    private String goodsName;
    private String goodsType;
    private String goodsDescription;
    private int currentQuantity;
    private int safeQuantity;
    private String picUrl;
    @TableField(exist = false)
    private MultipartFile pic;
}
