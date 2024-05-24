package com.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manage.entity.Goods;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface GoodsMapper extends BaseMapper<Goods> {
    @Select("SELECT DISTINCT goods_type FROM goods")
    List<String> selectDistinctGoodsTypes();

    @Select("SELECT * FROM goods WHERE current_quantity < quantity_mark")
    List<Goods> selectLowInventory();

    @Select("SELECT goods_type, SUM(current_quantity) as totalQuantity FROM goods GROUP BY goods_type")
    List<Map<String, Object>> getGoodsTypeAndQuantity();
}
