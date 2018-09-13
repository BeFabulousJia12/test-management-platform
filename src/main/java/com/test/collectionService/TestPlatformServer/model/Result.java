package com.test.collectionService.TestPlatformServer.model;

/**
 * @Author You Jia
 * @Date 8/2/2018 4:36 PM
 */
public class Result<T> {
    String code;
    String msg;
    T data;
    //normal Result construction
    public Result(String code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    //exception response construction
    public Result(ExceptionEnum exceptionEnum,T data){
        this.code = exceptionEnum.getCode();
        this.msg = exceptionEnum.getMsg();
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
