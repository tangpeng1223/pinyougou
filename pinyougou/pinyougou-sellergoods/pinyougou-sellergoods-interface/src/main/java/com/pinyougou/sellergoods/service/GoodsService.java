package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.Goods;
import com.pinyougou.vo.PageResult;

import java.util.List;

public interface GoodsService extends BaseService<TbGoods> {

    PageResult search(Integer page, Integer rows, TbGoods goods);

    /**
     * 添加商品
     * @param goods  封装的实体goods
     */
    void add(Goods goods);

    /**
     * 回显商品数据 通过商品id
     * @param id 商品id
     * @return  Goods（TbGoods GoodsDesc item）
     */
    Goods findGoodsById(Long id);

    /**
     * 保存更新的商品数据
     * @param goods 封装的商品数据
     */
    void updateGoods(Goods goods);

    /**
     * @param ids 提交审核商品的id
     * @param status 修改状态
     * @return Result 返回结果
     */
    void updateStatus(Long[] ids, String status);

    /**
     * 删除商品 本质修改is_delete该字段为1
     * @param ids 要删除的商品的id数组
     * @return Result 处理结果
     */
    void deleteGoodsByIds(Long[] ids);

    /**
     * 根据spu id数组和状态查询sku的集合
     * @param ids spu的id的素数组
     * @param status spu 的状态
     * @return List<TbItem>
     */
    List<TbItem> findItemListByGoodsIdsAndStatus(Long[] ids, String status);

    /**
     * 根据spu id和状态查询sku
     * @param goodsId spu的id
     * @param status spu 的状态
     * @return Goods
     */
    Goods findItemByGoodsIdAndStatus(Long goodsId, String status);
}