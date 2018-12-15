package com.pinyougou.mapper;

import com.pinyougou.pojo.TbBrand;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Date:2018/12/10
 * Author tangpeng
 * DESC: 品牌持久层
 */
public interface BrandMapper extends Mapper<TbBrand> {

    /**
     * 查询所有的品牌数据
     * @return
     */
    List<TbBrand> queryAll();

    /**
     * 查询品牌 格式为：
     * [{"id":1,"text":"联想"}{"id":11,"text":"诺基亚"}],
     **/
    List<Map<String, Object>> selectOptionList();
}
