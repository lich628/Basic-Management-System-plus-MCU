package com.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manage.entity.Batches;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Select("select batch_id from batches where operator_id = #{operatorId}")
    List<String> selectBatchIdsByOperatorId(String operatorId);

    @Update("UPDATE batches SET is_closed = 1 WHERE batch_id = #{batchId}")
    int closeBatch(String batchId);
}
