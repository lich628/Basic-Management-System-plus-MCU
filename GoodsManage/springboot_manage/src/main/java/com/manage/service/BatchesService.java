package com.manage.service;

import com.manage.mapper.BatchesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BatchesService {
    @Autowired
    BatchesMapper batchesMapper;
}
