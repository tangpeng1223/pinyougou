package com.pinyougou.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;

import java.io.Serializable;
import java.util.List;

/**
 * Date:2018/12/11
 * Author
 * DESC:
 */
public class BaseServiceImpl<T> implements BaseService<T> {

    @Autowired
    private Mapper<T> mapper;
    /**
     * 通过主键查询
     *
     * @param id
     * @return T
     */
    @Override
    public T findOne(Object id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public List<T> findAll() {
        return mapper.selectAll();
    }

    /**
     * 通过条件查询
     *
     * @param t
     * @return
     */
    @Override
    public List<T> findWhere(T t) {
        return mapper.select(t);
    }

    /**
     * 批量删除
     * @param ids
     */
    @Override
    public void deleteByIds(Serializable[] ids) {
        if(ids.length>0&& ids!=null){
            //批量删除
            for(Serializable id:ids){
                mapper.deleteByPrimaryKey(id);
            }
        }
    }

    /**
     * @param page 当前页号
     * @param rows 每页显示的数量
     * @return PageResult 分页包装类
     */
    @Override
    public PageResult findPage(Integer page, Integer rows) {
        PageHelper.startPage(page,rows);
        List<T> list = mapper.selectAll();
        //实例化分页信息
        PageInfo<T> pageInfo = new PageInfo<>(list);
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    /**
     * 带条件分页查询
     *
     * @param page
     * @param rows
     * @param t    查询条件
     * @return PageResult
     */
    @Override
    public PageResult findPage(Integer page, Integer rows, T t) {
        PageHelper.startPage(page,rows);
        //带条件查询
        List<T> list = mapper.select(t);
        //实例化分页信息
        PageInfo<T> pageInfo = new PageInfo<>(list);
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    /**
     * 更新数据
     *
     * @param t
     */
    @Override
    public void update(T t) {
        //灵活性修改数据
         mapper.updateByPrimaryKeySelective(t);
    }

    /**
     * 添加信息
     *
     * @param t
     */
    @Override
    public void add(T t) {
        //灵活性添加数据
       mapper.insertSelective(t);
    }
}
