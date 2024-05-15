package com.manage.controller;

import com.manage.entity.SensorData;
import com.manage.service.SensorDataService;
import com.manage.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/data")
public class SensorDataController {
    @Autowired
    SensorDataService sensorDataService;

    @GetMapping("/list")
    public Result listData(){
        System.out.println("SensorDataController->listData--> 开始查询数据");
        return Result.ok().data("sensorData",sensorDataService.getAllSensorData());
    }

    @PostMapping("/list")
    public Result insertData(@RequestBody SensorData data){
        System.out.println("SensorDataController->insertData--> 开始插入数据"+data);
        return sensorDataService.insertSensorData(data) == 1 ? Result.ok() : Result.error();
    }

    @DeleteMapping("/list/{id}")
    public Result deleteData(@PathVariable int id){
        System.out.println("SensorDataController->deleteData--> 开始删除数据");
        return sensorDataService.deleteSensorDataById(id) == 1 ? Result.ok() : Result.error();
    }

    @PostMapping("/start")
    public void start(){
        System.out.println("SensorDataController->start--> 开始读取数据");
        sensorDataService.startSerialPortReader();
    }
    @PostMapping("/stop")
    public void stop(){
        System.out.println("SensorDataController->stop--> 停止读取数据");
        sensorDataService.stopSerialPortReader();
    }
}
