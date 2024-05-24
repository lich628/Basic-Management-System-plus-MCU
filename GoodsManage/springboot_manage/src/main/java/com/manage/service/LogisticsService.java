package com.manage.service;

import com.manage.entity.Logistics;
import com.manage.mapper.LogisticsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogisticsService {
    @Autowired
    LogisticsMapper logisticsMapper;

    public List<Logistics> getAllLogistics(){
        return logisticsMapper.selectList(null);
    }

    public int addLogistics(Logistics logistics) {
        System.out.println("LogisticsService --> 新增物流记录");
        return logisticsMapper.insert(logistics);
    }

    public int updateLogistics(Logistics logistics) {
        System.out.println("LogisticsService --> 修改物流记录");
        return logisticsMapper.updateById(logistics);
    }

    public int deleteLogistics(int id) {
        System.out.println("LogisticsService --> 删除物流记录"+ id);
        return logisticsMapper.deleteById(id);
    }
}
