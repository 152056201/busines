package com.neuedu.busines.service.impl;

import com.neuedu.busines.common.Consts;
import com.neuedu.busines.common.RedisApi;
import com.neuedu.busines.common.ServerResponse;
import com.neuedu.busines.common.StatusEnum;
import com.neuedu.busines.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private RedisApi redisApi;
    @Value("${token.timeout}")
    Integer timeout;

    @Override
    public ServerResponse generateToken() {
        //生成token
        String token = UUID.randomUUID().toString();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Consts.TOKEN_PREFIX).append(token);
        //保存到redis
        redisApi.set(stringBuffer.toString(), stringBuffer.toString());
        if (stringBuffer.toString() != null && !stringBuffer.toString().equals("")) {
            return ServerResponse.serverResponseBySucess(null, stringBuffer.toString());
        }
        return ServerResponse.serverResponseByFail(StatusEnum.TOKEN_GENERA_FAIL.getCode(), StatusEnum.TOKEN_GENERA_FAIL.getMsg());
    }

    @Override
    public ServerResponse checkToken(HttpServletRequest request) {
        //从请求头获取token
        String token = request.getHeader(Consts.TOKEN_NAME);
        if (token == null || token.equals("")) {
            //如果为空 从参数中获得
            token = request.getParameter(Consts.TOKEN_NAME);
        }
        if (token == null || token.equals("")) {
            return ServerResponse.serverResponseByFail(StatusEnum.NOT_CARRY_TOKEN.getCode(), StatusEnum.NOT_CARRY_TOKEN.getMsg());
        }
        //校验token是否存在
        String value = redisApi.get(token);
        if (value == null || value.equals("")) {
            //token不在服务器，或者被消费了
            return ServerResponse.serverResponseByFail(StatusEnum.CANT_REPLEASE.getCode(),StatusEnum.CANT_REPLEASE.getMsg());
        }
        //token有效
        Long remove = redisApi.remove(token);
        if(remove==null){
            return ServerResponse.serverResponseByFail(StatusEnum.CANT_REPLEASE.getCode(),StatusEnum.CANT_REPLEASE.getMsg());
        }
        return ServerResponse.serverResponseBySucess();
    }
}
