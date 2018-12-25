package com.pinyougou.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyou.search.service.ItemSearchService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Date:2018/12/24
 * Author tangpeng
 * DESC: 根据条件查询商品
 */
@RestController
@RequestMapping("itemSearch")
public class ItemSearchController {

    @Reference
    private ItemSearchService itemSearchService;

    /**$http.post("itemSearch/search.do"+searchMap);
     * 根据搜索关键字搜索商品列表
     * @param searchMap 搜索条件
     * @return 搜索结果
     */
    @PostMapping("search")
    public Map<String,Object> search(@RequestBody Map<String,Object> searchMap){
        String  keywords = (String) searchMap.get("keywords");
        System.out.println(keywords);
        return itemSearchService.search(searchMap);

    }
}
