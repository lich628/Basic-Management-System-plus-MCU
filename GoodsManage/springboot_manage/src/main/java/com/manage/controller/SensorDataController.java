package com.manage.controller;

import com.manage.entity.SensorData;
import com.manage.service.SensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/data")
public class SensorDataController {
    @Autowired
    SensorDataService sensorDataService;

    @PostMapping("/insert")
    public void insertData(@RequestBody SensorData data){
        System.out.println("SensorDataController->insertData--> 开始插入数据"+data);
        sensorDataService.insertSensorData(data);
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
