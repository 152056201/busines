package com.neuedu.busines;

import com.neuedu.busines.interceptor.AutoIdCompomentInter;
import com.neuedu.busines.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Component
@SpringBootConfiguration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    LoginInterceptor loginInterceptor;
    @Autowired
    AutoIdCompomentInter autoIdCompomentInter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(autoIdCompomentInter);
        List<String> loginPath = new ArrayList<>();//需要拦截的路径
        loginPath.add("/manage/**");
        loginPath.add("/user/**");
        loginPath.add("/cart/**");
        loginPath.add("/order/**");

        List<String> excludePath = new ArrayList<>(); //不需要拦截的路径
        excludePath.add("/user/login");
        excludePath.add("/user/register");
        excludePath.add("/manage/product/upload");
        registry.addInterceptor(loginInterceptor).addPathPatterns(loginPath).excludePathPatterns(excludePath);
    }
}
