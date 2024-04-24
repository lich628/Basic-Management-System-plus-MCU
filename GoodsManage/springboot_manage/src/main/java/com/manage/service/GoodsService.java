package com.manage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.manage.entity.Goods;
import com.manage.mapper.GoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;


@Service
public class GoodsService {
    @Autowired
    GoodsMapper goodsMapper;

    // 增删改查等方法
    public List<Goods> goodsList(){
        System.out.println("GoodsService->goodsList--> 查询所有物资");
        return goodsMapper.selectList(null);
    }
    public List<Goods> goodsList(Goods goods){
        LambdaQueryWrapper<Goods> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Goods::getGoodsName, goods.getGoodsName());
        System.out.println("GoodsService->goodsList--> " + goods + " 物资名模糊查询");
        return goodsMapper.selectList(lambdaQueryWrapper);
    }
    public int addGoods(Goods goods){
        System.out.println("GoodsService->addGoods--> " + goods + " 新物资即将被插入");
        return goodsMapper.insert(goods);
    }
    public int deleteById(int id){
        System.out.println("GoodsService->deleteById--> id: " + id + " 物资即将被删除");
        return goodsMapper.deleteById(id);
    }
    public int updateById(Goods goods){
        System.out.println("GoodsService->updateById--> " + goods + " 物资即将被更新");
        return goodsMapper.updateById(goods);
    }
    public Goods selectById(int id){
        System.out.println("GoodsService->selectById--> id: " + id + " 物资查询");
        return goodsMapper.selectById(id);
    }

    public String storeFile(MultipartFile file) {
        System.out.println("GoodsService->storeFile--> " + file + " 开始运行存储程序...");
        String fileName = file.getOriginalFilename();
        String realPath = System.getProperty("user.dir") + "\\GoodsManage\\springboot_manage\\src\\main\\resources\\static\\img\\";
        System.out.println("GoodsService->storeFile--> " + realPath + " 此位置将被用作存储");
        File folder = new File(realPath);
        if (!folder.exists()) {
            System.out.println("GoodsService->storeFile--> " + realPath + " 正在创建文件夹！");
            //若不存在该目录，则创建目录
            folder.mkdirs();
        }
        String newName;
        if (fileName.endsWith(".png") || fileName.endsWith(".PNG")) {
            newName = fileName+".png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".JPG")) {
            newName = fileName+".jpg";
        } else if (fileName.endsWith(".jpeg") || fileName.endsWith(".JPEG")) {
            newName = fileName+".jpeg";
        } else {
            System.out.println("GoodsService->storeFile--> 文件类型不受支持！程序停止");
            return null;
        }
        try {
            System.out.println("GoodsService->storeFile--> " + folder + newName + " 正在存储图片");
            file.transferTo(new File(folder, newName));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("GoodsService->storeFile--> " + folder + newName + " 此位置存储失败！程序停止");
            return null;
        }
        System.out.println("GoodsService->storeFile--> " + folder + newName + " 成功存储新文件，返回主调函数");
        return newName;
    }

    public String getURL(String fileName, HttpServletRequest request) {
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/img/" + fileName;
        return url;
    }
}
