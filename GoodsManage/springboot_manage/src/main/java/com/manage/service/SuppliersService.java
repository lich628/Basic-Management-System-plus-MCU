package com.manage.service;

import com.manage.entity.Suppliers;
import com.manage.mapper.SuppliersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuppliersService {
    @Autowired
    SuppliersMapper suppliersMapper;

    public List<Suppliers> getAllSuppliers() {
        return suppliersMapper.selectList(null);
    }
    public int addSupplier(Suppliers supplier) {
        return suppliersMapper.insert(supplier);
    }
    public int updateSupplier(Suppliers supplier) {
        return suppliersMapper.updateById(supplier);
    }
    public int deleteSupplier(int id) {
        return suppliersMapper.deleteById(id);
    }
}
