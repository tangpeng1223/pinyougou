package com.pinyou.search.service;

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
}
