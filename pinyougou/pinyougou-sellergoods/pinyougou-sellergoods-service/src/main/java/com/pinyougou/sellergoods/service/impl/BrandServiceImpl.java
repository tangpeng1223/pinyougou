package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;


import com.pinyougou.pojo.TbBrand;
import com.pinyougou.mapper.BrandMapper;
import com.pinyougou.sellergoods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

/**
 * Date:2018/12/10
 * Author
 * DESC:
 */
@Service(interfaceClass = BrandService.class)
public class BrandServiceImpl implements BrandService {

    @Autowired
   private BrandMapper brandMapper;

    /**
     * 查询所有的品牌
     *
     * @return
     */
    @Override
    public List<TbBrand> queryAll() {
        return brandMapper.queryAll();
    }
}
