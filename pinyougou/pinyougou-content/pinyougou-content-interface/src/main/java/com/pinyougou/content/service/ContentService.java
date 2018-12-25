package com.pinyougou.content.service;

import com.pinyougou.pojo.TbContent;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.PageResult;

import java.util.List;

public interface ContentService extends BaseService<TbContent> {

    PageResult search(Integer page, Integer rows, TbContent content);

    /**
     * 门户系统大广告数据动态加载
     * @param categoryId 内容分类id
     * @return <TbContent>
     */
    List<TbContent> findContentListByCategoryId(Long categoryId);
}