package com.neuedu.busines.controller;

import com.neuedu.busines.common.Consts;
import com.neuedu.busines.common.ServerResponse;
import com.neuedu.busines.pojo.User;
import com.neuedu.busines.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping("/register")
    public ServerResponse register(User user){
        return userService.resgister(user);
    }
    @RequestMapping("/login")
    public ServerResponse login(String username, String password, HttpSession session){
        ServerResponse login = userService.login(username, password);
        if(login.isSucess()){
            session.setAttribute("user",login);
        }
        return login;

    }

    /**
     * 退出登录接口
     * */

    @RequestMapping("/logout")
    public ServerResponse logout(HttpSession session){

        session.removeAttribute(Consts.USER);

        return ServerResponse.serverResponseBySucess();

    }
}
