package com.imooc.mall.exception;

import com.imooc.mall.common.ApiRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者李时汇
 * s h a n L i a n g
 * 创建时间2021/10/24日 7:17
 * ----------------------
 * 类描述
 * 统一处理异常
 **/
@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handlerException(Exception e){
        log.error("DEFAULT EXCEPTION : "+ e);
        return ApiRestResponse.error(new ImoocMallException(ImoocMallExceptionEnum.SYSTEM_ERROR));
    }
    @ExceptionHandler(ImoocMallException.class)
    @ResponseBody
    public Object ImmocMallException(ImoocMallException e){
        log.error("mallException : "+ e.getMessage());
        return ApiRestResponse.error(e);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiRestResponse handleMethodArgumentNotValidExeception(MethodArgumentNotValidException e)
    {
        log.error("MethodArgumentNotValidException :",e);
       return handleBindingResult( e.getBindingResult());

    }

    //处理对外的提示
    private ApiRestResponse  handleBindingResult(BindingResult bindingResult){
        List<String> list=new ArrayList<>();
        if (bindingResult.hasErrors()) {

            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (int i = 0; i < allErrors.size(); i++) {
                ObjectError objectError = allErrors.get(i);
                String defaultMessage = objectError.getDefaultMessage();
                list.add(defaultMessage);

            }

            
        }
        if (list==null){
            return ApiRestResponse.error(new ImoocMallException(ImoocMallExceptionEnum.PARAM_WRONG));
        }
        return new ApiRestResponse(ImoocMallExceptionEnum.PARAM_WRONG.getCode(),ImoocMallExceptionEnum.PARAM_WRONG.getMsg(),list.toString());
    }
}
