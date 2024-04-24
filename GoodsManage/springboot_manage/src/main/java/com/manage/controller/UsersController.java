package com.manage.controller;

import com.manage.entity.Users;
import com.manage.service.UsersService;
import com.manage.utils.JwtUtils;
import com.manage.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UsersController {
    @Autowired
    UsersService usersService;

    @GetMapping("/list") //查询所有用户
    public Result list() {
        return Result.ok().data("users", usersService.usersList());
    }
    @PostMapping("/list") //按名字模糊查询用户
    public Result list(@RequestBody Users user) {
        return Result.ok().data("users", usersService.usersList(user));
    }

    @PostMapping("/list/add") //添加用户
    public Result add(Users user, HttpServletRequest request) {
        System.out.println("UsersController->add--> 开始添加用户");
        try{
            if (user.getFile() == null) {
                String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/avatar/";
                    if (user.getSex() == 0) {
                        user.setAvatar(url + "default_girl.png");
                    } else {
                        user.setAvatar(url + "default_boy.png");
                    }
            } else {
                user.setAvatar(usersService.getURL(usersService.storeFile(user.getFile()), request));
            }
            if (usersService.addUser(user) == 1) {
                System.out.println("UsersController->add--> 用户添加成功");
                return Result.ok().data("users", usersService.selectById(user.getUserId()));
            }
            if (usersService.addUser(user) == -1) {
                System.out.println("UsersController->add--> 用户已存在");
                return Result.error().msg("用户已存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error().msg("添加用户失败");
        }
        return Result.ok().data("users", usersService.usersList());
    }

    @PutMapping("/list") //修改用户信息
    public Result update(Users user, HttpServletRequest request) {
        System.out.println("UsersController->update--> 修改用户信息");
        try {
            if (user.getFile() != null) {
                user.setAvatar(usersService.getURL(usersService.storeFile(user.getFile()), request));
            }
            if (usersService.updateById(user) == 1) {
                System.out.println("UsersController->update--> 用户信息修改成功");
                return Result.ok().data("users", usersService.selectById(user.getUserId()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("UsersController->update--> 用户信息修改失败！");
            return Result.error().msg("出现严重错误，修改失败");
        }
        return Result.error().msg("修改用户信息失败");
    }

    @DeleteMapping("/list") //删除用户
    public Result delete(Users user) {
        System.out.println("UsersController->delete--> 删除用户");
        try {
            if (usersService.deleteById(user.getUserId()) == 1) {
                System.out.println("UsersController->delete--> 用户删除成功");
                return Result.ok().msg("用户删除成功");
            } else {
                System.out.println("UsersController->delete--> 删除失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error().msg("删除用户失败");
        }
        return Result.error().msg("删除用户失败");
    }

    @PostMapping("/login") //用户登录
    public Result login(@RequestBody Users user) {
        System.out.println("UsersController->login--> 用户登陆中...");
        String token = JwtUtils.generateToken(user.getUserAccount() + "&" + user.getPassword());
        String[] u = JwtUtils.getClaimsByToken(token).getSubject().split("&");
        String user_account = u[0];
        String password = u[1];
        System.out.println("UsersController->login--> user_account: " + user_account + " || password: " + password);

        int flag = usersService.validateUser(user);
        if (flag == 2) {
            return Result.ok().data("token", token); //成功则返回token
        } else if (flag == 1) {
            return Result.error().msg("密码错误！请重新输入");
        } else if (flag == 0) {
            return Result.warn().msg("用户未注册");
        }
        return Result.error().msg("出现严重错误，请关闭网页");
    }

    @GetMapping("/info") //使用token验证用户
    public Result info(String token) {
        System.out.println("UsersController->info--> 使用token查询用户...");
        String[] u = JwtUtils.getClaimsByToken(token).getSubject().split("&");
        String user_account = u[0];
        String password = u[1];
        System.out.println("UsersController->info--> token信息--> 用户账户: " + user_account + " & 密码: " + password);

        Users user = new Users(user_account, password);
        int flag = usersService.validateUser(user);
        if (flag == 2) {
            user = usersService.selectByAccount(user_account);
            System.out.println("UsersController->info--> 返回前端的信息--> " + user);
            return Result.ok().data("username", user.getUserAccount()).data("password", user.getPassword()).data("avatar", user.getAvatar());
        }
        return Result.error().msg("用户校验失败，请重新登录");
    }
}
