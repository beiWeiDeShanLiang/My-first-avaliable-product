package com.imooc.mall.filter;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 创建者李时汇
 * s h a n L i a n g
 * 创建时间2021/10/25日 0:23
 * ----------------------
 * ---------类描述--------
 * 管理员校验过滤器
 **/
public class AdminFilter implements Filter {
    @Autowired
    UserService userService;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();

        User currentUser = (User) session.getAttribute(Constant.MALL_USER);
        if (currentUser==null) {
            PrintWriter writer = new HttpServletResponseWrapper((HttpServletResponse) servletResponse).getWriter();
            writer.write( "{\n" +
                    "    \"status\": 10007,\n" +
                    "    \"msg\": \"need_login\",\n" +
                    "    \"data\": null\n" +
                    "}");
            writer.flush();
            writer.close();
            return ;
        }
        //校验是否是管理员
        if (!userService.checkAdminRole(currentUser)) {
            PrintWriter writer = new HttpServletResponseWrapper((HttpServletResponse) servletResponse).getWriter();
            writer.write( "{\n" +
                    "    \"status\": 10009,\n" +
                    "    \"msg\": \"not_admin\",\n" +
                    "    \"data\": null\n" +
                    "}");
            writer.flush();
            writer.close();
                   }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
