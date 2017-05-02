package com.liuwp.dao;

import com.liuwp.model.User;
import org.springframework.stereotype.Repository;

/**
 * Author: liuwuping
 * Date: 17/4/29
 * Description:
 */
@Repository
public interface UserMapper {

    User selectByName(String username);

    int insertUser(User user);
}
