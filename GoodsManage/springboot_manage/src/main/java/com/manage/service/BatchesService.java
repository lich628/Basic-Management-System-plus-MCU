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

    public Batches getBatchWithDetails(int batchId) {
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

    // 添加物资详情
    public List<Batches> batchesAddDetails(List<Batches> batches) {
        List<Integer> batchIds = new ArrayList<>();
        for (Batches batch : batches) {
            batchIds.add(batch.getBatchId());
        }
        List<Batches> resBatches = new ArrayList<>();
        for (int id : batchIds) {
            Batches batch = getBatchWithDetails(id);
            if (batch != null) {
                resBatches.add(batch);
            }
        }
        return resBatches;
    }

    // 获取目前有效batch，即is_closed字段不为1的batch
    public List<Batches> getAllActiveBatch() {
        List<Batches> allBatches = new ArrayList<>();
        List<Integer> batchIds = batchesMapper.selectActiveBatchesIds();
        System.out.println(batchIds);
        for (int batchId : batchIds) {
            Batches batch = getBatchWithDetails(batchId);
            if (batch != null) {
                allBatches.add(batch);
            }
        }
        return allBatches;
    }

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

    // 获取历史batch，即is_closed字段为0的batch
    public List<Batches> getClosedBatches() {
        List<Batches> closedBatches = new ArrayList<>();
        List<Integer> batchIds = batchesMapper.selectClosedBatchesIds();
        System.out.println(batchIds);
        for (int batchId : batchIds) {
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

    public int addBatch(Batches batch) {
        System.out.println("addBatch: " + batch.toString());
        return batchesMapper.insert(batch);
    }

    public int updateBatch(Batches batch) {
        System.out.println("updateBatch: " + batch.toString());
        return batchesMapper.updateById(batch);
    }

    public List<String> getOccupiedUids() {
        return batchesMapper.selectOccupiedUids();
    }

}
