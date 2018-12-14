package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.SpecificationMapper;
import com.pinyougou.mapper.SpecificationOptionMapper;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.sellergoods.service.SpecificationService;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.PageResult;
import com.pinyougou.vo.Result;
import com.pinyougou.vo.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;

@Service(interfaceClass = SpecificationService.class)
public class SpecificationServiceImpl extends BaseServiceImpl<TbSpecification> implements SpecificationService {

    @Autowired
    private SpecificationMapper specificationMapper; //规格持久层

    @Autowired
    private SpecificationOptionMapper specificationOptionMapper; //规格选项

    @Override
    public PageResult search(Integer page, Integer rows, TbSpecification specification) {
        PageHelper.startPage(page, rows);

        Example example = new Example(TbSpecification.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(specification.getSpecName())){
            criteria.andLike("specName", "%" + specification.getSpecName() + "%");
        }

        List<TbSpecification> list = specificationMapper.selectByExample(example);
        PageInfo<TbSpecification> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }


    /**
     * {"specificationOptionList":[{"optionName":"白色","orders":"1"},{"optionName":"蓝色","orders":"2"}],
     *    "specification":{"specName":"颜色"}}
     * @param specification
     * @return Result 处理结果
     */
    @Override
    public void add(Specification specification) {
        //添加规格
        specificationMapper.insertSelective(specification.getSpecification());
        //添加规格选项
        if(specification.getSpecificationOptionList().size()>0 && specification.getSpecificationOptionList()!=null){
           for(TbSpecificationOption specificationOption:specification.getSpecificationOptionList()){
               specificationOption.setSpecId(specification.getSpecification().getId()); //设置规格选项的id和规格id一致
               specificationOptionMapper.insertSelective(specificationOption);
           }
        }

    }

    /**
     * 通过id查询规格信息
     * @param id 规格id
     * @return Specification
     */
    @Override
    public Specification findOne(Long id) {

        Specification specification=new Specification();
        //查询出id的规格
        TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);
        specification.setSpecification(tbSpecification);
        //查出spec_Id=id的规格选项
        TbSpecificationOption specificationOption=new TbSpecificationOption();
        specificationOption.setSpecId(id);
        List<TbSpecificationOption> specificationOptionList = specificationOptionMapper.select(specificationOption);
        //设置规格选项
        specification.setSpecificationOptionList(specificationOptionList);
        return specification;
    }

    /**
     * 保存更新的数据
     *
     * @param specification
     * @return Result 处理结果
     */
    @Override
    public void update(Specification specification) {
        //更新规格信息
       specificationMapper.updateByPrimaryKeySelective(specification.getSpecification());
       //更新规格选项信息
        //删除原有的关联
        TbSpecificationOption param=new TbSpecificationOption();
        //设删除条件
        param.setSpecId(specification.getSpecification().getId());
        specificationOptionMapper.delete(param);
        //设置新的关联
        if(specification.getSpecificationOptionList().size()>0&& specification.getSpecificationOptionList()!=null){
            for(TbSpecificationOption specificationOption:specification.getSpecificationOptionList()){
                //设置关联id
                specificationOption.setSpecId(specification.getSpecification().getId());
                specificationOptionMapper.insertSelective(specificationOption);
            }
        }
    }

    /**
     *  批量删除规格及其关联的规格选项
     * @param ids 选中的规格id数组
     */
    @Override
    public void deleteSpecificationByIds(Long[] ids) {
           //删除规格表中数据
           deleteByIds(ids);
           //删除关联的规格选项数据
        Example example=new Example(TbSpecificationOption.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("specId", Arrays.asList(ids));
        specificationOptionMapper.deleteByExample(example);
    }
}
