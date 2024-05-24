package com.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manage.entity.Batches;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BatchesMapper extends BaseMapper<Batches>{

    @Select("select batch_id from batches where is_closed = 0 or is_closed is null")
    List<String> selectActiveBatchesIds();

    @Select("select batch_id from batches where is_closed = 1")
    List<String> selectClosedBatchesIds();

    @Select("select card_uid from batches where is_closed = 0 and card_uid is not null and card_uid != ''")
    List<String> selectOccupiedUids();

    @Insert("INSERT INTO batches (batch_id, batch_type, operator_id, op_time, op_comment) VALUES (#{batchId}, #{batchType}, #{operatorId}, #{opTime}, #{opComment})")
    int newBatch(Batches batch);

    @Update("UPDATE batches SET batch_status = #{batchStatus}, reviewer_id = #{reviewerId}, review_time = #{reviewTime}, viewer_comment = #{viewerComment}, card_uid = #{cardUid} WHERE batch_id = #{batchId}")
    int reviewBatch(Batches batch);

    @Select("select batch_id from batches where card_uid = #{cardUid} and is_closed = 0")
    String selectBatchIdByCardUid(String cardUid);

    @Select("SELECT batch_id FROM batches WHERE (operator_id = #{userId} OR reviewer_id = #{userId}) AND (is_closed = 0 OR is_closed IS NULL)")
    List<String> selectBatchIdsByUserId(String userId);

    @Update("UPDATE batches SET is_closed = 1 WHERE batch_id = #{batchId}")
    int closeBatch(String batchId);

    @Select("SELECT " +
            "SUM(CASE WHEN IFNULL(batch_status, 0) = 0 AND is_closed = 0 THEN 1 ELSE 0 END) AS pendingReview, " +
            "SUM(CASE WHEN IFNULL(batch_status, 0) = 1 AND batch_type = 'in' AND is_closed = 0 THEN 1 ELSE 0 END) AS pendingInStock, " +
            "SUM(CASE WHEN IFNULL(batch_status, 0) = 1 AND batch_type = 'out' AND is_closed = 0 THEN 1 ELSE 0 END) AS pendingOutStock " +
            "FROM batches")
    Map<String, Integer> getBatchStatistics();
}
