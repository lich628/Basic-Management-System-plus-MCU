package com.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manage.entity.Batches;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchesMapper extends BaseMapper<Batches>{

    @Select("select batch_id from batches where is_closed = 0 or is_closed is null")
    List<Integer> selectActiveBatchesIds();

    @Select("select batch_id from batches where is_closed = 1")
    List<Integer> selectClosedBatchesIds();

    @Select("select card_uid from batches where is_closed = 0 and card_uid is not null and card_uid != ''")
    List<String> selectOccupiedUids();
}
