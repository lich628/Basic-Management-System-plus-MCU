package com.manage.service;

import com.manage.mapper.batchGoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class batchGoodsService {
    @Autowired
    batchGoodsMapper batchGoodsMapper;
}
