package com.imooc.mall.common;

import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;

/**
 * 创建者李时汇
 * s h a n L i a n g
 * 创建时间2021/10/24:5:10
 * ----------------------
 *      类描述
 *  通用返回对象
 **/

public class ApiRestResponse<T> {
    private Integer status;
    private String msg;
    private T data;
    private static final int OK_CODE= 1000;
    private static final String OK_MSG="SUCCESS";

    public ApiRestResponse(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public ApiRestResponse(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
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

    public static int getOkCode() {
        return OK_CODE;
    }

    public static String getOkMsg() {
        return OK_MSG;
    }

    public ApiRestResponse() {
      this(OK_CODE,OK_MSG);
    }


    @Override
    public String toString() {
        return "ApiRestResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public static <T> ApiRestResponse<T> success(){
        return new ApiRestResponse<T>();
    }
    public static <T> ApiRestResponse<T> success(T result){
        ApiRestResponse<T> tApiRestResponse = new ApiRestResponse<>(OK_CODE, OK_MSG, result);
        return  tApiRestResponse;
    }
    public static <T> ApiRestResponse<T> error(ImoocMallException imoocMallExceptionEnum){
        return new ApiRestResponse<>(imoocMallExceptionEnum.getCode(),imoocMallExceptionEnum.getMessage());
    }
}
