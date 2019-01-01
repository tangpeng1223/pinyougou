package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pinyou.search.service.ItemSearchService;
import com.pinyougou.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.util.StringUtils;

import java.util.*;

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
            //处理多关键字空格问题
            searchMap.put("keywords",searchMap.get("keywords").toString().replaceAll(" ",""));
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

        //设置分页信息
        int pageNo=1;
        int pageSize=20;
        if(!StringUtils.isEmpty(searchMap.get("pageNo"))){
            pageNo=Integer.parseInt(searchMap.get("pageNo").toString());
        }
        if(!StringUtils.isEmpty(searchMap.get("pageSize"))){
          pageSize=Integer.parseInt(searchMap.get("pageSize").toString());
        }
        //设置每页起始索引
        query.setOffset((pageNo-1)*pageSize);
        //设置每页显示的数据
        query.setRows(pageSize);


        //过滤排序
        if(!StringUtils.isEmpty(searchMap.get("sortFiled")) && !StringUtils.isEmpty(searchMap.get("sortOrder"))){
            String sortFiled=searchMap.get("sortFiled").toString();
            String sortOrder=searchMap.get("sortOrder").toString();
            Sort sort=new Sort(sortOrder.equals("DESC")? Sort.Direction.DESC: Sort.Direction.ASC,"item_"+sortFiled);
            query.addSort(sort);
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
        //返回总页数
        searchResult.put("totalPages",scoredPage.getTotalPages());
        //返回总记录数
        searchResult.put("total",scoredPage.getTotalElements());
        //返回查询结果
        return searchResult;
    }

    /**
     * 导入更新后的商品数据
     * @param itemList sku集合 商品列表
     */
    @Override
    public void importItemList(List<TbItem> itemList) {
        for (TbItem tbItem : itemList) {
           Map  itemMap= JSON.parseObject(tbItem.getSpec(),Map.class);
           tbItem.setSpecMap(itemMap);
        }
        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();
    }

    /**
     * 修改spu后需要删除solr中的数据
     * @param ids 修改spu的id的数组
     */
    @Override
    public void deleteItemListByGoodsIds(Long[] ids) {
        Criteria criteria=new Criteria("item_goodsid").in(Arrays.asList(ids));
        SimpleQuery query=new SimpleQuery();
        query.addCriteria(criteria);
        solrTemplate.delete(query);
        solrTemplate.commit();
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
