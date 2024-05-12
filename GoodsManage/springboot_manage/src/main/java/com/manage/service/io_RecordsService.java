package com.manage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manage.entity.Users;
import com.manage.entity.io_Records;
import com.manage.mapper.io_RecordsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class io_RecordsService {
    @Autowired
    io_RecordsMapper io_recordsMapper;

    public List<io_Records> list(){
        return io_recordsMapper.selectList(null);
    }

    public IPage<io_Records> listSelect(String batchIdOrOperatorName, int pageNum, int pageSize){
        LambdaQueryWrapper<io_Records> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(batchIdOrOperatorName)) {
            lambdaQueryWrapper.and(wrapper -> wrapper.like(io_Records::getBatchId, batchIdOrOperatorName.trim())
                    .or()
                    .like(io_Records::getOperatorName, batchIdOrOperatorName.trim()));
        }
        Page<io_Records> page = new Page<>(pageNum, pageSize);
        System.out.println("ioRecordsService -> 条件查询io记录:"+"input输入为"+batchIdOrOperatorName);
        return io_recordsMapper.selectPage(page, lambdaQueryWrapper);
    }

    public int addRecord(io_Records io_records){
        return io_recordsMapper.insert(io_records);
    }

    public int deleteRecord(String id){
        return io_recordsMapper.deleteById(id);
    }

}
