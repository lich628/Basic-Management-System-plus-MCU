package com.manage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.manage.entity.*;
import com.manage.mapper.BatchesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BatchesService {
    @Autowired
    BatchesMapper batchesMapper;
    @Autowired
    batchGoodsService batchGoodsService;
    @Autowired
    GoodsService GoodsService;

    public List<Batches> getBatches(){
        return batchesMapper.selectList(null);
    }
    //------------------根据批次id查询批次详情，且附带batchGoods------------------------
    public Batches getBatchWithDetails(String batchId) {
        Batches batches = batchesMapper.selectById(batchId);
        if (batches == null) {
            return null;
        }
        try {
            List<batchGoods> batchGoodsList = batchGoodsService.getBatchGoodsByBatchId(batchId);
            List<batchGoodsDetail> batchGoodsDetails = new ArrayList<>();
            for (batchGoods bg : batchGoodsList) {
                Goods goods = GoodsService.selectById(bg.getGoodsId());
                batchGoodsDetail batchGoodsDetail = new batchGoodsDetail();
                batchGoodsDetail.setGoodsId(bg.getGoodsId());
                batchGoodsDetail.setGoodsName(goods.getGoodsName());
                batchGoodsDetail.setQuantity(bg.getQuantity());
                batchGoodsDetail.setPicUrl(goods.getPicUrl());

                batchGoodsDetails.add(batchGoodsDetail);
            }
            batches.setBatchGoodsDetail(batchGoodsDetails);
        } catch (Exception e) {
            System.out.println("getBatchWithDetails error, batchId: " + batchId);
            e.printStackTrace();
        }
        return batches;
    }

    //------------------传入batches对象列表，把其全部加上物资详情（batchGoods）------------------------
    public List<Batches> batchesAddDetails(List<Batches> batches) {
        List<String> batchIds = new ArrayList<>();
        for (Batches batch : batches) {
            batchIds.add(batch.getBatchId());
        }
        List<Batches> resBatches = new ArrayList<>();
        for (String id : batchIds) {
            Batches batch = getBatchWithDetails(id);
            if (batch != null) {
                resBatches.add(batch);
            }
        }
        return resBatches;
    }

    //-------------------获取目前有效batch，即is_closed字段不为1的batch-----------------------
    public List<Batches> getAllActiveBatch() {
        List<Batches> allBatches = new ArrayList<>();
        List<String> batchIds = batchesMapper.selectActiveBatchesIds();
        System.out.println(batchIds);
        for (String batchId : batchIds) {
            Batches batch = getBatchWithDetails(batchId);
            if (batch != null) {
                allBatches.add(batch);
            }
        }
        return allBatches;
    }

    //-------------------获取目前有效batch，即is_closed字段不为1的batch（条件查询）-----------------------
    public List<Batches> getAllReqBatch(String batchInfoInput, String batchType, String batchStatus) {
        LambdaQueryWrapper<Batches> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.isNull(Batches::getIsClosed).or().eq(Batches::getIsClosed, 0);
        if (StringUtils.isNotBlank(batchInfoInput)) {
            lambdaQueryWrapper.and(wrapper -> wrapper.like(Batches::getBatchId,  batchInfoInput.trim())
                    .or()
                    .like(Batches::getCardUid, batchInfoInput.trim()));
        }
        if (StringUtils.isNotBlank(batchType)) {
            lambdaQueryWrapper.eq(Batches::getBatchType, batchType.trim());
        }
        if (StringUtils.isNotBlank(batchStatus)) {
            lambdaQueryWrapper.eq(Batches::getBatchStatus, batchStatus.trim());
        }
        List<Batches> batchesList = batchesMapper.selectList(lambdaQueryWrapper);
        System.out.println("batch筛选条件为:"+batchInfoInput+" "+batchType+" "+batchStatus);
        return batchesAddDetails(batchesList);
    }

    //-----------------------获取历史batch，即is_closed字段为0的batch------------------------
    public List<Batches> getClosedBatches() {
        List<Batches> closedBatches = new ArrayList<>();
        List<String> batchIds = batchesMapper.selectClosedBatchesIds();
        System.out.println(batchIds);
        for (String batchId : batchIds) {
            Batches batch = getBatchWithDetails(batchId);
            if (batch != null) {
                closedBatches.add(batch);
            }
        }
        return closedBatches;
    }

    public List<Batches> getReqClosedBatch(String batchId, String batchType, String batchStatus, String cardUid) {
        LambdaQueryWrapper<Batches> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Batches::getIsClosed, 1);
        if (StringUtils.isNotBlank(batchId)) {
            lambdaQueryWrapper.like(Batches::getBatchId, batchId.trim());
        }
        if (StringUtils.isNotBlank(batchType)) {
            lambdaQueryWrapper.eq(Batches::getBatchType, batchType.trim());
        }
        if (StringUtils.isNotBlank(batchStatus)) {
            lambdaQueryWrapper.eq(Batches::getBatchStatus, batchStatus.trim());
        }
        if (StringUtils.isNotBlank(cardUid)) {
            lambdaQueryWrapper.eq(Batches::getCardUid, cardUid.trim());
        }
        List<Batches> closedBatchesList = batchesMapper.selectList(lambdaQueryWrapper);
        System.out.println("历史batch筛选:"+batchId+" "+batchType+" "+batchStatus+" "+cardUid);
        return batchesAddDetails(closedBatchesList);
    }
    //----------------------新增物资批次-------------------------------
    public int addBatch(Batches batch) {
        try{
            batchesMapper.newBatch(batch);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
        // 遍历batchGoodsDetail列表，为每个batchGoods对象设置batchId，然后保存
        List<batchGoodsDetail> goodsList = batch.getBatchGoodsDetail();
        for (batchGoodsDetail bgd : goodsList) {
            batchGoods bg = new batchGoods();
            bg.setBatchId(batch.getBatchId());
            bg.setGoodsId(bgd.getGoodsId());
            bg.setQuantity(bgd.getQuantity());
            batchGoodsService.addBatchGoods(bg);
        }
        return 1;
    }

    public int updateBatch(Batches batch) {
        System.out.println("更新batch: " + batch.toString());
        try{
            batchesMapper.reviewBatch(batch);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    public List<String> getOccupiedUids() {
        return batchesMapper.selectOccupiedUids();
    }

}
