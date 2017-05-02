package com.liuwp.dao;

import com.liuwp.model.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: liuwuping
 * Date: 17/4/29
 * Description:
 */
@Repository
public interface RoleMapper {

    List<Role> selectByUserId(Integer userId);
}
