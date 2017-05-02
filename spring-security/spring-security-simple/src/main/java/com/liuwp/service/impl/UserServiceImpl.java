package com.liuwp.service.impl;

import com.liuwp.dao.RoleMapper;
import com.liuwp.dao.UserMapper;
import com.liuwp.model.Role;
import com.liuwp.model.User;
import com.liuwp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: liuwuping
 * Date: 17/4/29
 * Description:
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userMapper.selectByName(s);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        } else {
//        List<Role> roles = roleMapper.selectByUserId(user.getId());
            List<Role> roles = new ArrayList<>();
            Role role = new Role();
            role.setId(1);
            role.setName("admin");
            roles.add(role);
            List<GrantedAuthority> authorities = getUserAuthority(roles);
            return buildUserForAuthentication(user, authorities);
        }
    }

    @Override
    public User getByName(String name) {
        return userMapper.selectByName(name);
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insertUser(user);

    }

    private List<GrantedAuthority> getUserAuthority(List<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
        for (Role role : userRoles) {
            roles.add(new SimpleGrantedAuthority(role.getName()));
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>(roles);
        return grantedAuthorities;
    }

    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true, true, true, true, authorities);
    }
}
