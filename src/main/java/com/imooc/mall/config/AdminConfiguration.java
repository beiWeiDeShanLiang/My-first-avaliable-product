package com.imooc.mall.config;

import com.imooc.mall.filter.AdminFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;

/**
 * 创建者李时汇
 * s h a n L i a n g
 * 创建时间2021/10/25日 1:44
 * ----------------------
 * ---------类描述--------
 * admin过滤器配置
 **/
@Configuration
public class AdminConfiguration {
    @Bean
    public AdminFilter adminFilter(){
        return new AdminFilter();
    }
    @Bean("adminFilterConf")
    public FilterRegistrationBean adminFilterConfig(){
        FilterRegistrationBean filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(adminFilter());
        filterFilterRegistrationBean.addUrlPatterns("/admin/category/*");
        filterFilterRegistrationBean.addUrlPatterns("/admin/product/*");
        filterFilterRegistrationBean.addUrlPatterns("/admin/order/*");
        filterFilterRegistrationBean.setName("adminFilterConf");
        return filterFilterRegistrationBean;
    }

}
