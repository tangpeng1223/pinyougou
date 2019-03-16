package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.mapper.BrandMapper;
import com.pinyougou.sellergoods.service.BrandService;
import com.pinyougou.service.BaseService;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;


import java.util.List;
import java.util.Map;

/**
 * Date:2018/12/10
 * Author
 * DESC:
 */
@Service(interfaceClass = BrandService.class)
public class BrandServiceImpl extends BaseServiceImpl<TbBrand> implements BrandService {

    @Autowired
   private BrandMapper brandMapper;

    /**
     * 查询所有的品牌
     * @return
     */
    @Override
    public List<TbBrand> queryAll() {
        System.out.println("");
        return brandMapper.queryAll();
        System.out.println("");
    }

    /**
     * 带搜索条件查询
     *
     * @param tbBrand 查询条件
     * @param page    当前页
     * @param rows    页大小数据
     * @return
     */
    @Override
    public PageResult search(TbBrand tbBrand, Integer page, Integer rows) {
        //开启分页
        PageHelper.startPage(page,rows);
        Example example=new Example(TbBrand.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(tbBrand.getFirstChar())){
            criteria.andEqualTo("firstChar",tbBrand.getFirstChar());
        }
        if(!StringUtils.isEmpty(tbBrand.getName())){
            criteria.andLike("name","%"+tbBrand.getName()+"%");
        }

        List<TbBrand> tbBrands = brandMapper.selectByExample(example);
        PageInfo<TbBrand> pageInfo = new PageInfo<>(tbBrands);
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    /**
     * 查询品牌 格式为：
     * [{"id":1,"text":"联想"}{"id":11,"text":"诺基亚"}],
     **/
    @Override
    public List<Map<String, Object>> selectOptionList() {
        return brandMapper.selectOptionList();
    }
}
