package com.manage.service;

import com.manage.entity.Orders;
import com.manage.mapper.OrdersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersService {
    @Autowired
    OrdersMapper ordersMapper;

    public List<Orders> getAllOrders() {
        return ordersMapper.selectList(null);
    }

    public int addOrder(Orders order) {
        return ordersMapper.insert(order);
    }

    public int updateOrder(Orders order) {
        return ordersMapper.updateById(order);
    }

    public int deleteOrder(int id) {
        return ordersMapper.deleteById(id);
    }
}
