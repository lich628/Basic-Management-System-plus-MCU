package com.manage.controller;

import com.manage.entity.Logistics;
import com.manage.service.LogisticsService;
import com.manage.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/logistics")
public class LogisticController {
    @Autowired
    LogisticsService logisticsService;

    @GetMapping("/getAllLogistics")
    public Result getAllOrders() {
        return Result.ok().data("logistics",logisticsService.getAllLogistics());
    }

    @PostMapping("/addLogistics")
    public Result addOrder(@RequestBody Logistics logistics) {
        return logisticsService.addLogistics(logistics) == 1 ? Result.ok() : Result.error();
    }

    @PutMapping("/updateLogistics")
    public Result updateOrder(@RequestBody Logistics logistics) {
        return logisticsService.updateLogistics(logistics) == 1 ? Result.ok() : Result.error();
    }

    @DeleteMapping("/deleteLogistics/{id}")
    public Result deleteOrder(@PathVariable int id) {
        return logisticsService.deleteLogistics(id) == 1 ? Result.ok() : Result.error();
    }
}
