package com.liuwp.service;

import com.liuwp.model.User;

/**
 * Author: liuwuping
 * Date: 17/4/29
 * Description:
 */
public interface UserService {

    User getByName(String name);

    void saveUser(User user);
}
