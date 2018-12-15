package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.PageResult;
import com.pinyougou.vo.Result;
import com.pinyougou.vo.Specification;

import java.util.List;
import java.util.Map;

public interface SpecificationService extends BaseService<TbSpecification> {

    PageResult search(Integer page, Integer rows, TbSpecification specification);

    /**
     * {"specificationOptionList":[{"optionName":"白色","orders":"1"},{"optionName":"蓝色","orders":"2"}],
     *    "specification":{"specName":"颜色"}}
     * @param specification
     * @return Result 处理结果
     */
    void add(Specification specification);

    /**
     * 通过id查询规格信息
     * @param id 规格id
     * @return Specification
     */
    Specification findOne(Long id);


    /**
     * 保存更新的数据
     * @param specification
     * @return Result 处理结果
     */
    void update(Specification specification);

    /**
     *  批量删除规格及其关联的规格选项
     * @param ids 选中的规格id数组
     */
    void deleteSpecificationByIds(Long[] ids);

    /**
     * 查询所有的规格 格式为：[{"text":"内存大小"},{"text":"颜色"}]
     * @return  List<Map<String ,Object>>
     */
    List<Map<String, Object>> selectOptionList();
}