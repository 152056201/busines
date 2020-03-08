package com.neuedu.busines.interceptor;

import com.google.gson.Gson;
import com.neuedu.busines.annotation.AutoIdCompoment;
import com.neuedu.busines.common.ServerResponse;
import com.neuedu.busines.common.StatusEnum;
import com.neuedu.busines.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

@Component
public class AutoIdCompomentInter implements HandlerInterceptor {
    @Autowired
    TokenService tokenService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        AutoIdCompoment annotation = method.getAnnotation(AutoIdCompoment.class);
        if (annotation != null) {
            //该方法上有注解
            //对token进行验证
            ServerResponse serverResponse = tokenService.checkToken(request);
            if(!serverResponse.isSucess()){
                PrintWriter printWriter = null;
                try {
                    response.reset();
                    response.addHeader("Content-Type","application/json;charset=UTF-8");
                    printWriter = response.getWriter();
                    Gson gson = new Gson();
                    String toJson = gson.toJson(serverResponse);
                    printWriter.write(toJson);
                    printWriter.flush();
                    printWriter.close();
                    return false;
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if (printWriter!=null){
                        printWriter.close();
                    }

                }
            }else {
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
