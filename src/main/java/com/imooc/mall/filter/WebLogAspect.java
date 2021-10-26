package com.imooc.mall.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 创建者李时汇
 * s h a n L i a n g
 * 创建时间2021/10/24:4:14
 * ----------------------
 * 类描述
 * 打印请求和相应信息
 **/
@Aspect
@Component
public class WebLogAspect {
    private static StringBuffer sb  ;
    private final Logger log=LoggerFactory.getLogger(WebLogAspect.class);
    static{
        StringBuffer s   =new StringBuffer();
        sb=s.append("ARGS :");
    }
    @Pointcut("execution(public * com.imooc.mall.controller..*.*(..))")
    public void webLog(){}
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        log.info("URL :"+request.getRequestURI());
        log.info("IP :"+request.getRemoteAddr());
        log.info("CLASS_METHOD :"+joinPoint.getSignature().getDeclaringTypeName()+
                "."+joinPoint.getSignature().getName());
        log.info("Http_Method :"+request.getMethod().toString());
        Object[] args = joinPoint.getArgs();
        sb.delete(6,sb.length());
        for (Object arg : args) {
            sb.append(arg.toString()+"  ||  ");
        }

        log.info( sb.toString());

    }
    @AfterReturning(returning = "res",pointcut = "webLog()")
    public void  doAfterReturning(Object res) throws JsonProcessingException {
         log.info("RESPONSE =" +new ObjectMapper().writeValueAsString(res));
    }
}
