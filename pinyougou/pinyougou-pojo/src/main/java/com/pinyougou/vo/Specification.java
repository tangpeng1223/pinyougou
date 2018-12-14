package com.pinyougou.vo;

import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationOption;

import java.io.Serializable;
import java.util.List;

/**
 * Date:2018/12/13
 * Author  唐鹏
 * DESC:  封装添加规格信息
 */
public class Specification implements Serializable {


    private TbSpecification specification; //规格名
    private List<TbSpecificationOption> specificationOptionList; //规格选项


    public void setSpecification(TbSpecification specification) {
        this.specification = specification;
    }

    public void setSpecificationOptionList(List<TbSpecificationOption> specificationOptionList) {
        this.specificationOptionList = specificationOptionList;
    }

    public List<TbSpecificationOption> getSpecificationOptionList() {
        return specificationOptionList;
    }

    public TbSpecification getSpecification() {
        return specification;
    }
}

