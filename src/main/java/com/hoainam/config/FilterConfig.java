package com.hoainam.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean; 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hoainam.filter.AuthFilter;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilterRegistration(AuthFilter authFilter) {
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(authFilter);

        registrationBean.addUrlPatterns("/admin/*");

        registrationBean.setOrder(1);
        return registrationBean;
    }
}
