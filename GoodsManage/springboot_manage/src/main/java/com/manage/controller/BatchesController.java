package com.manage.controller;

import com.manage.entity.Batches;
import com.manage.service.BatchesService;
import com.manage.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/batches")
public class BatchesController {
    @Autowired
    BatchesService batchesService;

    @GetMapping("/list")
    public Result getAllActiveBatch() {
        return Result.ok().data("batches", batchesService.getAllActiveBatch());
    }

    @GetMapping("/listLike") // 按名字模糊查询物资
    public Result list(@RequestParam(required = false) String batchInfoInput,
                       @RequestParam(required = false) String batchType,
                       @RequestParam(required = false) String batchStatus) {
        return Result.ok().data("batches", batchesService.getAllReqBatch(batchInfoInput, batchType, batchStatus));
    }

    @PostMapping("/list")
    public Result addBatch(@RequestBody Batches batch) {
        return Result.ok().data("batches", batchesService.addBatch(batch));
    }

    @GetMapping("/occupiedUids")
    public Result OccupiedUids() {
        System.out.println(batchesService.getOccupiedUids());
        return Result.ok().data("occupiedUid", batchesService.getOccupiedUids());
    }

    @PutMapping("/list")
    public Result updateBatch(@RequestBody Batches batch) {
        return Result.ok().data("batches", batchesService.updateBatch(batch));
    }

}
