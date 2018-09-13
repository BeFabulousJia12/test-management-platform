package com.test.collectionService.TestPlatformServer.model;

/**
 * @Author You Jia
 * @Date 8/2/2018 4:39 PM
 */
public enum ExceptionEnum {
    TESTRESULT_NOT_FOUND("10000","Test Results Not Found!"),
    TESTCASE_NOT_FOUND("20000","Test Cases Not Found!"),
    ACCOUNT_DUPLICATE("4001","Account is duplicate!"),
    DATA_NOT_FOUND("4002","No Data!"),
    LOGIN_ERROR("3001","username or password incorrect!"),
    SESSION_INVALID("3002","Please login");

    private String code;
    private String msg;

    ExceptionEnum(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
