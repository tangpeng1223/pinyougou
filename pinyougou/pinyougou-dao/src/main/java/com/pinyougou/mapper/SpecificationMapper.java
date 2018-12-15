package com.pinyougou.mapper;

import com.pinyougou.pojo.TbSpecification;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface SpecificationMapper extends Mapper<TbSpecification> {

    /**
     * 查询所有的规格 格式为：[{"text":"内存大小"},{"text":"颜色"}]
     * @return List<Map < String   , O bject>>
     */
    List<Map<String, Object>> selectOptionList();
}
