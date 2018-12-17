package com.pinyougou.shop.service.impl;

import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Date:2018/12/15
 * Author 唐鹏
 * DESC:   自定义认证类
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * 因为是在父容器中 不能使用注解来注入
     */
    private SellerService sellerService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //创建权限集合
        List<GrantedAuthority> authorites=new ArrayList<>();
        //添加权限
        authorites.add(new SimpleGrantedAuthority("ROLE_SELLER"));

        //根据用户名查询密码
        TbSeller seller = sellerService.findOne(username);
        //用户不为空 且审核通过才放行
        if(seller!=null && "1".equals(seller.getStatus())){
            return new User(username,seller.getPassword(),authorites);
        }

        //没有用户返回空
       return null;
    }

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }
}
