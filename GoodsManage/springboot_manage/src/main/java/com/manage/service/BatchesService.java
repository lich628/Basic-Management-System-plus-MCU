package com.manage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.manage.entity.*;
import com.manage.mapper.BatchesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public Map<String, Integer> getBatchStatistics() {
        return batchesMapper.getBatchStatistics();
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
        System.out.println("BatchesService --> 查询批次");
        for (String batchId : batchIds) {
            Batches batch = getBatchWithDetails(batchId);
            if (batch != null) {
                allBatches.add(batch);
            }
        }
        return allBatches;
    }

    //-------------------获取目前有效batch，即is_closed字段不为1的batch（条件查询）-----------------------
    public List<Batches> getReqBatch(String batchInfoInput, String batchType, String batchStatus) {
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
        System.out.println("批次筛选条件为:"+batchInfoInput+" "+batchType+" "+batchStatus);
        return batchesAddDetails(batchesList);
    }

    //-----------------------获取历史batch，即is_closed字段为0的batch------------------------
    public List<Batches> getClosedBatches() {
        List<Batches> closedBatches = new ArrayList<>();
        List<String> batchIds = batchesMapper.selectClosedBatchesIds();
        System.out.println("BatchesService --> 查询历史批次");
        for (String batchId : batchIds) {
            Batches batch = getBatchWithDetails(batchId);
            if (batch != null) {
                closedBatches.add(batch);
            }
        }
        return closedBatches;
    }

    public List<Batches> getReqClosedBatch(String batchInfoInput, String batchType, String batchStatus) {
        LambdaQueryWrapper<Batches> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Batches::getIsClosed, 1);
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
        System.out.println("历史批次筛选条件为:"+batchInfoInput+" "+batchType+" "+batchStatus);
        return batchesAddDetails(batchesList);
    }

    public List<Batches> getBatchesByUserId(int userId) {
        List<Batches> userBatches = new ArrayList<>();
        List<String> batchIds = batchesMapper.selectBatchIdsByUserId(String.valueOf(userId));
        System.out.println("BatchesService --> 用户相关批次");
        for (String batchId : batchIds) {
            Batches batch = getBatchWithDetails(batchId);
            if (batch != null) {
                userBatches.add(batch);
            }
        }
        return userBatches;
    }

    public List<Batches> getReqUserBatch(int userId, String batchInfoInput, String batchType, String batchStatus) {
        LambdaQueryWrapper<Batches> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 未关闭的批次
        lambdaQueryWrapper.eq(Batches::getIsClosed, 0);
        // 用户相关的批次（操作员或审核员）
        lambdaQueryWrapper.and(wrapper ->
                wrapper.eq(Batches::getOperatorId, userId)
                        .or()
                        .eq(Batches::getReviewerId, userId)
        );
        // 批次信息输入条件
        if (StringUtils.isNotBlank(batchInfoInput)) {
            lambdaQueryWrapper.and(wrapper ->
                    wrapper.like(Batches::getBatchId, batchInfoInput.trim())
                            .or()
                            .like(Batches::getCardUid, batchInfoInput.trim())
            );
        }
        // 批次类型条件
        if (StringUtils.isNotBlank(batchType)) {
            lambdaQueryWrapper.eq(Batches::getBatchType, batchType.trim());
        }
        // 批次状态条件
        if (StringUtils.isNotBlank(batchStatus)) {
            lambdaQueryWrapper.eq(Batches::getBatchStatus, batchStatus.trim());
        }
        System.out.println("用户批次筛选条件为: " + batchInfoInput + " " + batchType + " " + batchStatus);
        List<Batches> batchesList = batchesMapper.selectList(lambdaQueryWrapper);
        return batchesAddDetails(batchesList);
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

        //-------------------------更新物资批次------------------------------
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

    public String getBatchIdByCardUid(String cardUid) {
        System.out.println("BatchesService -> getBatchIdByCardUid: " + cardUid);
        return batchesMapper.selectBatchIdByCardUid(cardUid);
    }

    public List<String> getOccupiedUids() {
        return batchesMapper.selectOccupiedUids();
    }

    public int closeBatch(String batchId) {
        System.out.println("暂存区删除批次"+ batchId);
        return batchesMapper.closeBatch(batchId);
    }

    //-------------------------关闭批次并更新物资数量------------------------------
    public int closeBatchAndUpdateGoods(String batchId) {
        Batches batch = batchesMapper.selectById(batchId);
        if (batch == null || batch.getIsClosed() == 1) {
            return 0; // 批次不存在或已关闭
        }
        List<batchGoods> batchGoodsList = batchGoodsService.getBatchGoodsByBatchId(batchId);
        for (batchGoods bg : batchGoodsList) {
            Goods goods = GoodsService.selectById(bg.getGoodsId());
            if (goods != null) {
                if ("in".equals(batch.getBatchType())) {
                    System.out.println("BatchService -> 入库:"+goods.getGoodsName()+" "+bg.getQuantity());
                    goods.setCurrentQuantity(goods.getCurrentQuantity() + bg.getQuantity());
                } else if ("out".equals(batch.getBatchType())) {
                    if (goods.getCurrentQuantity() - bg.getQuantity() < 0) {
                        System.out.println("BatchService -> 出库失败:"+goods.getGoodsName()+" "+bg.getQuantity());
                        return -1; // 出库数量大于当前库存
                    }
                    System.out.println("BatchService -> 出库:"+goods.getGoodsName()+" "+bg.getQuantity());
                    goods.setCurrentQuantity(goods.getCurrentQuantity() - bg.getQuantity());
                }
                GoodsService.updateById(goods); // 更新物资数量
            }
        }

        batch.setIsClosed(1);
        batchesMapper.updateById(batch); // 更新批次状态

        return 1;
    }

}
