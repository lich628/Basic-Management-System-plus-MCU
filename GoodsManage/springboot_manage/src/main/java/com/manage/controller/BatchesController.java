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
        System.out.println("添加物资批次 --> " + batch);
        return Result.ok().msg(batchesService.addBatch(batch) == 1 ? "新增成功":"新增失败");
    }

    @GetMapping("/occupiedUids")
    public Result OccupiedUids() {
        System.out.println(batchesService.getOccupiedUids());
        return Result.ok().data("occupiedUid", batchesService.getOccupiedUids());
    }

    @PutMapping("/list")
    public Result updateBatch(@RequestBody Batches batch) {
        System.out.println("更新物资批次 --> " + batch);
        return Result.ok().msg(batchesService.updateBatch(batch) == 1? "更新成功":"更新失败");
    }

    @GetMapping("/list/selectByUid")
    public Result getBatchWithCardUid(@RequestParam String cardUid) {
        String batchId = batchesService.getBatchIdByCardUid(cardUid);
        if (batchId == null) {
            return Result.warn().msg("未找到对应批次");
        }
        return Result.ok().data("batches", batchesService.getBatchWithDetails(batchId));
    }

    @PutMapping("/closeBatch")
    public Result closeBatch(@RequestParam String batchId) {
        System.out.println("处理批次 --> " + batchId);
        int result = batchesService.closeBatchAndUpdateGoods(batchId);
        if (result == 0) {
            System.out.println("批次不存在或者批次已处理");
            return Result.warn().msg("批次不存在或者批次已处理");
        } else if (result == -1) {
            System.out.println("非法库存数量操作");
            return Result.error().msg("非法库存数量操作");
        } else {
            System.out.println("批次close操作成功");
            return Result.ok().msg("操作成功");
        }
    }

}
