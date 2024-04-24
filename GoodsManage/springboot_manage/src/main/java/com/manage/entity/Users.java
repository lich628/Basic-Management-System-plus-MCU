package com.manage.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class Users {
    @TableId(type = IdType.AUTO)
    private int userId;
    private String userAccount;
    private String userName;
    private String password;
    private int age;
    private int sex;
    private int role;
    private String phone;
    private String isValid;
    private String avatar;
    @TableField(exist = false)
    private MultipartFile file;

    public Users(String userAccount, String password) {
        this.userAccount = userAccount;
        this.password = password;
    }
}
