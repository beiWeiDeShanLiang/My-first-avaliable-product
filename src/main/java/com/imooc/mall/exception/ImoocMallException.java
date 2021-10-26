package com.imooc.mall.exception;

/**
 * 创建者李时汇
 * s h a n L i a n g
 * 创建时间2021/10/24日 6:24
 * ----------------------
 * 类描述
 **/
public class ImoocMallException extends RuntimeException{
     private final Integer code;
     private final String message;
     public ImoocMallException(Integer code,String message)
     {
         this.code=code;
         this.message=message;

     }
     public ImoocMallException (ImoocMallExceptionEnum imoocMallExceptionEnum)
     {
         this(imoocMallExceptionEnum.code,imoocMallExceptionEnum.msg);
     }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
