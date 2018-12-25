package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyou.search.service.ItemSearchService;
import com.pinyougou.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Date:2018/12/24
 * Author
 * DESC:
 */
@Service
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;
    
    /**
     * 根据搜索关键字搜索商品列表
     * @param searchMap 搜索条件
     * @return 搜索结果
     */
    @Override
    public Map<String, Object> search(Map<String, Object> searchMap) {
        //创建map集合存放返回数据
        Map<String,Object> searchResult=new HashMap<>();

        //设置高亮查询对象
        SimpleHighlightQuery query = new SimpleHighlightQuery();
        //设置查询条件
        if(!StringUtils.isEmpty(searchMap.get("keywords"))){
            Criteria criteriaBrand=new Criteria("item_keywords").is(searchMap.get("keywords"));
            query.addCriteria(criteriaBrand);
        }else{
            Criteria criteria=new Criteria("item_keywords");
            query.addCriteria(criteria);
        }

        //设置过滤条件查询
        //过滤分类查询
        if(!StringUtils.isEmpty(searchMap.get("category"))){
            Criteria criteriaCategory=new Criteria("item_category").is(searchMap.get("category"));
            SimpleFilterQuery simpleFilterQuery = new SimpleFilterQuery(criteriaCategory);
            query.addFilterQuery(simpleFilterQuery);
        }
        //根据品牌过滤查询
        if(!StringUtils.isEmpty(searchMap.get("brand"))){
            Criteria brandCriteria=new Criteria("item_brand").is(searchMap.get("brand"));
            SimpleFilterQuery brandQuery = new SimpleFilterQuery(brandCriteria);
            query.addFilterQuery(brandQuery);
        }

        //根据规格过滤查询
        if(searchMap.get("spec")!=null){
            //得到规格集合
            Map<String ,Object> specMap= (Map<String, Object>) searchMap.get("spec");
            Set<Map.Entry<String, Object>> entrySet = specMap.entrySet();
            for (Map.Entry<String, Object> entry : entrySet) {
                //solr配置文件中是item_spec_*
                Criteria criteriaSpec=new Criteria("item_spec_"+entry.getKey()).is(entry.getValue());
                SimpleFilterQuery specCriteria=new SimpleFilterQuery(criteriaSpec);
                query.addFilterQuery(specCriteria);
            }
        }


        //过滤价格查询
        if(!StringUtils.isEmpty(searchMap.get("price"))){
            String[] price = searchMap.get("price").toString().split("-");
            //设置起始价格
            Criteria startCriteria=new Criteria("item_price").greaterThanEqual(price[0]);
            SimpleFilterQuery startQuery=new SimpleFilterQuery(startCriteria);
            query.addFilterQuery(startQuery);

            //如果选择的是又上限的价格区间
            if(!"*".equals(price[1])){
                //设置起始价格
                Criteria endCriteria=new Criteria("item_price").lessThanEqual(price[1]);
                SimpleFilterQuery endQuery=new SimpleFilterQuery(endCriteria);
                query.addFilterQuery(endQuery);
            }
        }

        //设置高亮标签
        HighlightOptions highlightOptions = new HighlightOptions();
        //设置高亮域
        highlightOptions.addField("item_title");
        highlightOptions.setSimplePrefix("<font style='color:red'>");
        highlightOptions.setSimplePostfix("</font>");

        //设置高亮标签
        query.setHighlightOptions(highlightOptions);

        //设置高亮查询
        HighlightPage<TbItem> scoredPage = solrTemplate.queryForHighlightPage(query, TbItem.class);

        //处理高亮标题
        List<HighlightEntry<TbItem>> highlighted = scoredPage.getHighlighted();
        if(highlighted!=null && highlighted.size()>0) {
            for (HighlightEntry<TbItem> entry : highlighted) {
                List<HighlightEntry.Highlight> highlights = entry.getHighlights();
                if (highlights!=null&& highlights.size()>0 && highlights.get(0).getSnipplets()!=null) {
                    //设置高亮标题
                    entry.getEntity().setTitle(highlights.get(0).getSnipplets().get(0));
                }
            }
        }

        //将查询的数据封装
        searchResult.put("rows",scoredPage.getContent());
        //返回查询结果
        return searchResult;
    }

   /* public Map<String, Object> search(Map<String, Object> searchMap) {
        //创建map集合存放返回数据
        Map<String,Object> searchResult=new HashMap<>();
        //设置查询条件
        Criteria criteria=new Criteria("item_keywords").is(searchMap.get("keywords"));
        SimpleQuery query=new SimpleQuery();
        query.addCriteria(criteria);
        //ScoredPage<TbItem> scoredPage = solrTemplate.queryForPage(query, TbItem.class);
        //将查询的数据封装
        searchResult.put("rows",scoredPage.getContent());
        //返回查询结果
        return searchResult;
    }*/

}
