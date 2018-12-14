package com.pinyougou.service;

import com.pinyougou.vo.PageResult;

import java.io.Serializable;
import java.util.List;

/**
 * Date:2018/12/11
 * Author
 * DESC:
 */
public interface BaseService<T> {
    /**
     * 通过主键查询
     *
     * @param id
     * @return T
     */
    T findOne(Object id);

    /**
     * 查询所有
     *
     * @return
     */
    List<T> findAll();

    /**
     * 通过条件查询
     *
     * @param t
     * @return
     */
    List<T> findWhere(T t);

    /**
     * 批量删除
     *
     * @param ids
     */
    void deleteByIds(Serializable[] ids);

    /**
     * @param page 当前页号
     * @param rows 每页显示的数量
     * @return PageResult
     */
    PageResult findPage(Integer page, Integer rows);

    /**
     * 带条件分页查询
     *
     * @param page
     * @param rows
     * @param t    查询条件
     * @return PageResult
     */
    PageResult findPage(Integer page, Integer rows, T t);

    /**
     * 更新数据
     *
     * @param t
     */
    void update(T t);

    /**
     * 添加信息
     *
     * @param t
     */
    void add(T t);
}