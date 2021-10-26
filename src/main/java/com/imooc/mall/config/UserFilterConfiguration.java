package com.imooc.mall.config;

import com.imooc.mall.filter.AdminFilter;
import com.imooc.mall.filter.UserFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 创建者李时汇
 * s h a n L i a n g
 * 创建时间2021/10/25日 1:44
 * ----------------------
 * ---------类描述--------
 * admin过滤器配置
 **/
@Configuration
public class UserFilterConfiguration {
    @Bean
    public UserFilter userFilter(){
        return new UserFilter();
    }
    @Bean("userFilterConf")
    public FilterRegistrationBean adminFilterConfig(){
        FilterRegistrationBean filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(userFilter());
        filterFilterRegistrationBean.addUrlPatterns("/cart/*");
        filterFilterRegistrationBean.addUrlPatterns("/order/*");
        filterFilterRegistrationBean.setName("userFilterConf");
        return filterFilterRegistrationBean;
    }

}
