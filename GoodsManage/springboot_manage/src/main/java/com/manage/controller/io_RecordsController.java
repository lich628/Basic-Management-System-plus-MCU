package com.manage.controller;

import com.manage.entity.io_Records;
import com.manage.service.io_RecordsService;
import com.manage.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/ioRecords")
public class io_RecordsController {
    @Autowired
    io_RecordsService io_recordsService;

    @GetMapping("/list")
    public Result list(){
        System.out.println("io_RecordsController->查询所有io记录");
        return Result.ok().data("ioRecords", io_recordsService.list());
    }

    @GetMapping("/listPage")
    public Result list(@RequestParam(required = false) String batchIdOrOperatorName,
                       @RequestParam(defaultValue = "1") int pageNum,
                       @RequestParam(defaultValue = "6") int pageSize){
        System.out.println("io_RecordsController->分页查询所有io记录");
        return Result.ok().data("ioRecords", io_recordsService.listSelect(batchIdOrOperatorName, pageNum, pageSize));
    }

    @PostMapping("/list")
    public Result addRecord(@RequestBody io_Records io_records){
        System.out.println("io_RecordsController->addRecord--> 开始插入数据"+io_records);
        if(io_recordsService.addRecord(io_records) == 1){
            System.out.println("io_RecordsController->addRecord--> 插入数据成功");
            return Result.ok().data("ioRecords", io_recordsService.list());
        }
        else{
            System.out.println("io_RecordsController->addRecord--> 插入数据失败");
            return Result.error().msg("插入数据失败");
        }
    }

    @DeleteMapping("/list/{ioId}")
    public Result deleteRecord(@PathVariable int ioId){
        System.out.println("io_RecordsController->deleteRecord--> 开始删除数据, id为"+ioId);
        return io_recordsService.deleteRecord(ioId) == 1 ? Result.ok() : Result.error();

    }
}
