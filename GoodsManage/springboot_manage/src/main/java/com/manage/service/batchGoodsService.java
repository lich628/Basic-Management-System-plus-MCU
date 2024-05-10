package com.manage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.manage.entity.batchGoods;
import com.manage.mapper.batchGoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class batchGoodsService {
    @Autowired
    batchGoodsMapper batchGoodsMapper;

    public List<batchGoods> getBatchGoods(){
        return batchGoodsMapper.selectList(null);
    }
    // 根据批次id查询批次物资包含的物资与数量
    public List<batchGoods> getBatchGoodsByBatchId(String batchId){
        LambdaQueryWrapper<batchGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(batchGoods::getBatchId, batchId);
        return batchGoodsMapper.selectList(queryWrapper);
    }

    public int addBatchGoods(batchGoods batchGoods){
        return batchGoodsMapper.insert(batchGoods);
    }

}
