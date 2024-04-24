package com.manage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.manage.entity.Users;
import com.manage.mapper.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class UsersService {
    @Autowired
    UsersMapper usersMapper;

    public List<Users> usersList(){
        System.out.println("UsersService->usersList--> 查询所有用户");
        return usersMapper.selectList(null);
    }

    public List<Users> usersList(Users users){
        LambdaQueryWrapper<Users> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Users::getUserName, users.getUserName());
        System.out.println("UsersService->usersList--> " + users + " 用户名模糊查询");
        return usersMapper.selectList(lambdaQueryWrapper);
    }

    public int addUser(Users user){
        System.out.println("UsersService->addUser--> " + user + " 新用户即将被插入");
        if (selectByAccount(user.getUserAccount()) == null) {
            return usersMapper.insert(user);
        } else {
            return -1;
        }
    }

    public Users selectByAccount(String userAccount) {
        System.out.println("UsersService->selectByAccount--> " + userAccount + " 查询用户账号");
        QueryWrapper<Users> wrapper = new QueryWrapper();
        wrapper.eq("user_account", userAccount);
        return usersMapper.selectOne(wrapper);
    }

    public Users selectById(int id) {
        System.out.println("UsersService->selectById--> id: " + id);
        return usersMapper.selectById(id);
    }

    public int getIdByAccount(Users user) {
        System.out.println("UsersService->getIdByAccount--> " + user + "根据账号获取用户id");
        QueryWrapper<Users> wrapper = new QueryWrapper();
        wrapper.eq("user_account", user.getUserAccount());
        Users um = usersMapper.selectOne(wrapper);
        return um.getUserId();
    }
    public int deleteById(int id){
        System.out.println("UsersService->deleteById--> id: " + id + " 用户即将被删除");
        return usersMapper.deleteById(id);
    }

    public int updateById(Users user) {
        System.out.println("UsersService->updateById--> " + user + " 用户即将被更新");
        return usersMapper.updateById(user);
    }

    public int validateUser(Users user) {
        Users um = selectByAccount(user.getUserAccount());
        //比较密码
        if (um == null) {
            System.out.println("UsersService->validateUser--> " + user + "查无此用户");
            return 0;//没有这个用户
        } else if (!um.getPassword().equals(user.getPassword())) {
            System.out.println("UsersService->validateUser--> " + user + "密码错误");
            return 1;//密码不对
        }
        System.out.println("UsersService->validateUser--> " + user + "用户密码匹配");
        return 2;//对
    }

    public String getURL(String fileName, HttpServletRequest request) {
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/img/" + fileName;
        return url;
    }

    public String storeFile(MultipartFile file) {
        System.out.println("UsersService->storeFile--> " + file + " 保存用户头像");
        String fileName = file.getOriginalFilename();
        String realPath = System.getProperty("user.dir") + "\\GoodsManage\\springboot_manage\\src\\main\\resources\\static\\avatar\\";
        System.out.println("UsersService->storeFile--> " + realPath + " 此位置将被用作存储");
        File folder = new File(realPath);
        if (!folder.exists()) {
            System.out.println("UsersService->storeFile--> " + realPath + " 正在创建文件夹！");
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
            System.out.println("UsersService->storeFile--> 文件类型不受支持！程序停止");
            return null;
        }
        try {
            System.out.println("UsersService->storeFile--> " + folder + newName + " 正在存储图片");
            file.transferTo(new File(folder, newName));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("UsersService->storeFile--> " + folder + newName + " 此位置存储失败！程序停止");
            return null;
        }
        System.out.println("UsersService->storeFile--> " + folder + newName + " 成功存储新文件，返回主调函数");
        return newName;
    }

}
