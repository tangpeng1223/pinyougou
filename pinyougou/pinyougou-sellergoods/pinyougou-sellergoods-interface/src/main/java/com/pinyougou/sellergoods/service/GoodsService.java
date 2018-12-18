package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbGoods;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.Goods;
import com.pinyougou.vo.PageResult;

public interface GoodsService extends BaseService<TbGoods> {

    PageResult search(Integer page, Integer rows, TbGoods goods);

    /**
     * 添加商品
     * @param goods  封装的实体goods
     */
    void add(Goods goods);
}