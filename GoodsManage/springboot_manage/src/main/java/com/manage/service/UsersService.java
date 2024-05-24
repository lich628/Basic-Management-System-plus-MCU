package com.manage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.manage.entity.Users;
import com.manage.mapper.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class UsersService {
    @Autowired
    UsersMapper usersMapper;

    public IPage<Users> usersList(int pageNum, int pageSize){
        System.out.println("UsersService->usersList--> 查询所有用户");
        Page<Users> page = new Page<>(pageNum, pageSize);
        return usersMapper.selectPage(page, null);
    }

    public IPage<Users> usersList(String userNameOrAccount, String sex, String role, int pageNum, int pageSize){
        LambdaQueryWrapper<Users> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(userNameOrAccount)) {
            lambdaQueryWrapper.and(wrapper -> wrapper.like(Users::getUserName, userNameOrAccount.trim())
                    .or()
                    .like(Users::getUserAccount, userNameOrAccount.trim()));
        }
        if(StringUtils.isNotBlank(sex)) {
            lambdaQueryWrapper.eq(Users::getSex, sex.trim().equalsIgnoreCase("男") ? 1 : 0);
        }
        if (StringUtils.isNotBlank(role)) {
            switch (role.trim()) {
                case "管理员":
                    lambdaQueryWrapper.eq(Users::getRole, 0);
                    break;
                case "审核员":
                    lambdaQueryWrapper.eq(Users::getRole, 1);
                    break;
                default:
                    lambdaQueryWrapper.eq(Users::getRole, 2);
                    break;
            }
        }
        Page<Users> page = new Page<>(pageNum, pageSize);
        System.out.println("UsersService->usersListLike--> 条件查询用户"+"|"+userNameOrAccount+"|"+sex+"|"+role);
        return usersMapper.selectPage(page, lambdaQueryWrapper);
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

    public List<Map<String, Object>> getUserCountsByRole() {
        return usersMapper.getUserCountsByRole();
    }

    public Users selectById(int id) {
        System.out.println("UsersService->selectById--> id: " + id);
        return usersMapper.selectById(id);
    }

    public Users selectNameAndAccount(int id){
        return usersMapper.selectNameAndAccountById(id);
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
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/avatar/" + fileName;
        return url;
    }

    public String storeFile(MultipartFile file, String userName) {
        System.out.println("UsersService->storeFile--> " + file + " 保存用户头像");
        String fileName = file.getOriginalFilename();
        String baseName = fileName.substring(0, fileName.lastIndexOf(".")); // 获取不包含扩展名的文件名
        String realPath = System.getProperty("user.dir") + "\\GoodsManage\\springboot_manage\\src\\main\\resources\\static\\avatar\\";
        System.out.println("UsersService->storeFile--> " + realPath + " 此位置将被用作存储");
        File folder = new File(realPath);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String formatDateTime = now.format(formatter);
        if (!folder.exists()) {
            System.out.println("UsersService->storeFile--> " + realPath + " 正在创建文件夹！");
            //若不存在该目录，则创建目录
            folder.mkdirs();
        }
        String newName;
        if (fileName.endsWith(".png") || fileName.endsWith(".PNG")) {
            newName = userName + "_" + formatDateTime + "_" + baseName +".png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".JPG")) {
            newName = userName + "-" + formatDateTime + "_" + baseName +".jpg";
        } else if (fileName.endsWith(".jpeg") || fileName.endsWith(".JPEG")) {
            newName = userName + "_" + formatDateTime + "_" + baseName +".jpeg";
        } else {
            System.out.println("UsersService->storeFile--> 文件类型不受支持！程序停止");
            return null;
        }
        try {
            System.out.println("UsersService->storeFile--> " + folder +"\\"+ newName + " 正在存储图片");
            file.transferTo(new File(folder, newName));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("UsersService->storeFile--> " + folder+"\\"+newName + " 此位置存储失败！程序停止");
            return null;
        }
        System.out.println("UsersService->storeFile--> " + folder + newName + " 成功存储新文件，返回主调函数");
        return newName;
    }

}
