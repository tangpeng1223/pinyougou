package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.PageResult;

import java.util.List;

/**
 * Date:2018/12/10
 * Author tangpeng
 * DESC:  品牌业务层接口
 */
public interface BrandService extends BaseService<TbBrand> {

    /**
     * 查询所有的品牌
     * @return
     */
    List<TbBrand> queryAll();

    /**
     * 带搜索条件查询
     * @param tbBrand 查询条件
     * @param page  当前页
     * @param rows  页大小数据
     * @return
     */
    PageResult search(TbBrand tbBrand, Integer page, Integer rows);
}
