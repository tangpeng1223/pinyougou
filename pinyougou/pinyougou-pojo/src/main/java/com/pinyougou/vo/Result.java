package com.pinyougou.vo;

import java.io.Serializable;

/**
 * Date:2018/12/12
 * Author 唐鹏
 * DESC:  处理crud成功和失败返回结果
 */
public class Result implements Serializable {

    private Boolean success;  //处理结果
    private String message; //返回的信息

    public Result(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    //处理成功的结果
    public static Result Success(String message) {
        return new Result(true,message);
    }

    //处理失败返回信息
    public static Result fail(String message) {
        return new Result(false,message);
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
