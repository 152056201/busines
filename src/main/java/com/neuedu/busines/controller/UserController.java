package com.neuedu.busines.controller;

import com.neuedu.busines.common.Consts;
import com.neuedu.busines.common.ServerResponse;
import com.neuedu.busines.common.StatusEnum;
import com.neuedu.busines.pojo.User;
import com.neuedu.busines.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * @param user
     * @return
     */
    @RequestMapping("/register")
    public ServerResponse register(User user){
        return userService.resgister(user);
    }

    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping("/login")
    public ServerResponse login(String username,String password,HttpSession session){
        ServerResponse login = userService.login(username, password);
        if(login.isSucess()){
            session.setAttribute(Consts.USER,login.getData());
        }
        return login;

    }

    /**
     * 更新信息
     */
    @RequestMapping("/update_information.do")
    public ServerResponse updateInfo(@RequestParam(value = "username",required = true) String username, String email,
                                     String phone,
                                     String question,
                                     String answer, User user, HttpSession session){
        user = (User)session.getAttribute(Consts.USER);
        ServerResponse serverResponse = userService.updateInfo(username,email,phone,question,answer);
        return serverResponse;
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
