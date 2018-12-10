package com.pinyougou.mapper;

import com.pinyougou.pojo.TbBrand;

import java.util.List;

/**
 * Date:2018/12/10
 * Author tangpeng
 * DESC: 品牌持久层
 */
public interface BrandMapper {

    /**
     * 查询所有的品牌数据
     * @return
     */
    List<TbBrand> queryAll();
}
