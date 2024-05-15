package com.manage.controller;

import com.manage.entity.Goods;
import com.manage.entity.Users;
import com.manage.service.GoodsService;
import com.manage.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @GetMapping("/list") // 查询所有物资
    public Result list(@RequestParam(defaultValue = "1") int pageNum,
                       @RequestParam(defaultValue = "6") int pageSize) {
        return Result.ok().data("goods", goodsService.goodsList(pageNum, pageSize));
    }
    @GetMapping("/listLike") // 按名字模糊查询物资
    public Result list(@RequestParam(required = false) String goodsName,
                       @RequestParam(required = false) String goodsType,
                       @RequestParam(defaultValue = "1") int pageNum,
                       @RequestParam(defaultValue = "6") int pageSize) {
        return Result.ok().data("goods", goodsService.goodsList(goodsName, goodsType, pageNum, pageSize));
    }

    @GetMapping("/list/type") // 查询所有物资类型
    public Result listType() {
        return Result.ok().data("goodsTypes", goodsService.goodsTypeList());
    }

    @PostMapping("/list") //添加物资
    public Result add(Goods goods, HttpServletRequest request) {
        System.out.println("GoodsController->add--> 开始进行物资添加");
        try {
            if (goods.getPic() == null) {
                goods.setPicUrl(null);
            }
            else {
                goods.setPicUrl(goodsService.getURL(goodsService.storeFile(goods.getPic()), request));
            }
            if (goodsService.addGoods(goods) != -1) {
                System.out.println("GoodsController->add--> 物资添加成功");
                return Result.ok().data("goods", goodsService.selectById(goods.getGoodsId()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("GoodsController->add--> 物资添加失败！");
            return Result.error().msg("出现严重错误，请关闭网页");
        }
        return Result.error().msg("添加物资失败");
    }
    @PutMapping("/list") //修改物资信息
    public Result update(@RequestBody Goods goods, HttpServletRequest request) {
        System.out.println("GoodsController->update--> 修改物资信息");
        try {
            if (goods.getPic() != null) {
                goods.setPicUrl(goodsService.getURL(goodsService.storeFile(goods.getPic()), request));
            }
            if (goodsService.updateById(goods) == 1) {
                System.out.println("GoodsController->update--> 物资信息修改成功");
                return Result.ok().data("goods", goodsService.selectById(goods.getGoodsId()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("GoodsController->update--> 物资信息修改失败！");
            return Result.error().msg("出现严重错误，修改失败");
        }
        return Result.error().msg("修改物资信息失败");
    }
    @DeleteMapping("/list") //删除物资
    public Result delete(@RequestBody Goods goods) {
        System.out.println("GoodsController->delete--> 删除物资");
        try {
            if (goodsService.deleteById(goods.getGoodsId()) == 1) {
                System.out.println("GoodsController->delete--> 物资删除成功");
                return Result.ok().msg("物资删除成功");
            } else {
                System.out.println("GoodsController->delete--> 删除失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("GoodsController->delete--> 物资删除失败！");
            return Result.error().msg("出现严重错误，删除失败");
        }
        return Result.error().msg("删除物资失败");
    }

    @GetMapping("/list/low") // 查询库存告急的物资
    public Result low() {
        return Result.ok().data("goods", goodsService.selectLowInventory());
    }

    @GetMapping("/listAll")
    public Result listAll() {
        return Result.ok().data("goods", goodsService.goodsList());
    }
}
