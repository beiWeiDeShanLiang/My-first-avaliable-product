package com.imooc.mall.filter;

import com.imooc.mall.common.Constant;
import com.imooc.mall.model.pojo.User;

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
 * 创建时间2021/10/26日 16:27
 * ----------------------
 * ---------类描述--------
 **/
public class UserFilter implements Filter {

    public static User currentUser;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        currentUser =(User) session.getAttribute(Constant.MALL_USER);
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
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
