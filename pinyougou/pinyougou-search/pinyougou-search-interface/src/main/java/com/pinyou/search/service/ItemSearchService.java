package com.pinyou.search.service;

import com.pinyougou.pojo.TbItem;

import java.util.List;
import java.util.Map;

/**
 * Date:2018/12/24
 * Author tangpeng
 * DESC: 根据过滤条件查询商品
 */
public interface ItemSearchService {
    /**
     * 根据搜索关键字搜索商品列表
     * @param searchMap 搜索条件
     * @return 搜索结果
     */
    Map<String, Object> search(Map<String, Object> searchMap);

    /**
     * 导入更新后的商品数据
     * @param itemList sku集合
     */
    void importItemList(List<TbItem> itemList);

    /**
     * 修改spu后需要删除solr中的数据
     * @param ids 修改spu的id的数组
     */
    void deleteItemListByGoodsIds(Long[] ids);
}
