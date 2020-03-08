package com.neuedu.busines.config;

import com.google.gson.Gson;
import com.neuedu.busines.common.RedisApi;
import com.neuedu.busines.common.ServerResponse;
import com.neuedu.busines.utils.MD5Utils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class RedisAspect {
    @Autowired
    private RedisApi redisApi;

    @Pointcut("execution(public * com.neuedu.busines.service.impl.CategoryServiceImpl.get*(..))")
    public void category() {
    }

    @Around("category()")
    public Object around(ProceedingJoinPoint point) {
        StringBuffer buffer = new StringBuffer();
        String className = point.getSignature().getDeclaringType().getName(); //类名
        buffer.append(className);
        String methodName = point.getSignature().getName();//方法名
        buffer.append(methodName);
        Object[] args = point.getArgs(); //参数
        for (Object o : args) {
            buffer.append(o);
        }
        //缓存key
        String cacheKey = MD5Utils.getMD5Code(buffer.toString());
        //从缓存根据key得到value
        String cacheValue = redisApi.get(cacheKey);
        Gson gson = new Gson();
        if (cacheValue == null) { //如何缓存数据为空
            //从数据库读取
            try {
                Object proceed = point.proceed(); //执行目标方法
                //将对象转为字符串
                String toJson = gson.toJson(proceed);
                //写入缓存
                redisApi.set(cacheKey, toJson);
                return proceed;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else { //有缓存数据
            ServerResponse serverResponse = gson.fromJson(cacheValue, ServerResponse.class);
            return serverResponse;
        }
        return null;
    }
}
