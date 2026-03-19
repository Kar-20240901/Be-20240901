package com.kar20240901.be.base.web.model.bo.base;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class BaseUserInsertBatchByExcelBO {

    @ExcelProperty(value = "昵称")
    private String nickname;

    @ExcelProperty(value = "用户名")
    private String username;

    @ExcelProperty(value = "邮箱")
    private String email;

    @ExcelProperty(value = "手机号码")
    private String phone;

    @ExcelProperty(value = "微信appId")
    private String wxAppId;

    @ExcelProperty(value = "微信openId")
    private String wxOpenId;

    @ExcelProperty(value = "微信unionId")
    private String wxUnionId;

    /**
     * 前端加密之后的原始密码，后端用
     */
    @ExcelProperty(value = "密码")
    private String originPassword;

    /**
     * 前端加密之后的密码
     */
    @ExcelIgnore
    private String password;

    @ExcelProperty(value = "个人简介")
    private String bio;

}
