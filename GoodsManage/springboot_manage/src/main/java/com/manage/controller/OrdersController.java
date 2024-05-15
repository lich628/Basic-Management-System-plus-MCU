package com.manage.controller;

import com.manage.entity.Orders;
import com.manage.service.OrdersService;
import com.manage.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/orders")
public class OrdersController {
    @Autowired
    OrdersService ordersService;

    @GetMapping("/getAllOrders")
    public Result getAllOrders() {
        return Result.ok().data("orders", ordersService.getAllOrders());
    }

    @PostMapping("/addOrder")
    public Result addOrder(@RequestBody Orders order) {
        return ordersService.addOrder(order) == 1 ? Result.ok() : Result.error();
    }

    @PutMapping("/updateOrder")
    public Result updateOrder(@RequestBody Orders order) {
        return ordersService.updateOrder(order) == 1 ? Result.ok() : Result.error();
    }

    @DeleteMapping("/deleteOrder/{orderId}")
    public Result deleteOrder(@PathVariable int orderId) {
        return ordersService.deleteOrder(orderId) == 1 ? Result.ok() : Result.error();
    }

}
