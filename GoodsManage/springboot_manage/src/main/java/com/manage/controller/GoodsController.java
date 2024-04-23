package com.manage.controller;

import com.manage.entity.Goods;
import com.manage.service.GoodsService;
import com.manage.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @GetMapping("/goods")
    public Result list() {
        return Result.ok().data("goods", goodsService.goodsList());
    }
    @PostMapping("/goods")
    public Result add(Goods goods, HttpServletRequest request) {
        System.out.println("GoodsController->add--> 开始运行...");
        System.out.println("GoodsController->add--> " + goods.getPic().getOriginalFilename());
        try {
            goods.setPicUrl(goodsService.getURL(goodsService.storeFile(goods.getPic()), request));
            if (goodsService.addGoods(goods) != -1) {
                System.out.println("GoodsController->add--> insert成功");
                return Result.ok().data("picUrl", goods.getPicUrl());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("GoodsController->add--> insert失败！");
            return Result.error().msg("出现严重错误，请关闭网页");
        }
        return Result.ok().data("goods", goodsService.goodsList());
    }
}
