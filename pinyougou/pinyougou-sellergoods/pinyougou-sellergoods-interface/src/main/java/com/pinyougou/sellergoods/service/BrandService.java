package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;

import java.util.List;

/**
 * Date:2018/12/10
 * Author tangpeng
 * DESC:  品牌业务层接口
 */
public interface BrandService {

    /**
     * 查询所有的品牌
     * @return
     */
    List<TbBrand> queryAll();
}
