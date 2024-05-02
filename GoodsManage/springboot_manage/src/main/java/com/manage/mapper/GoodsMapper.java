package com.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manage.entity.Goods;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsMapper extends BaseMapper<Goods> {
    @Select("SELECT DISTINCT goods_type FROM goods")
    List<String> selectDistinctGoodsTypes();
}
