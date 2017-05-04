package com.liuwp.controller;

import com.liuwp.model.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author:liuwp
 * Date: 2017/5/3
 * Description:
 */
@RestController
public class ResourceController {

    @RequestMapping(value = "/api/me", method = RequestMethod.GET)
    public User get() {
        User user = new User();
        user.setId(1L);
        user.setUsername("liuwp");
        user.setPassword("123456");
        return user;
    }


}
