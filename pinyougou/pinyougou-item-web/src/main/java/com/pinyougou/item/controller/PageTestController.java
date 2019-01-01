package com.pinyougou.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.sellergoods.service.ItemCatService;
import com.pinyougou.vo.Goods;
import freemarker.template.Configuration;
import freemarker.template.Template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Date:2018/12/28
 * Author tangpeng
 * DESC: 模拟文件静态化
 */
@RequestMapping("/test")
@RestController
public class PageTestController {

    @Value("${ITEM_HTML_PATH}")
    private String ITEM_HTML_PATH;


    @Reference
    private GoodsService goodsService;

    @Reference
    private ItemCatService itemCatService;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

   @GetMapping("/audit")
   public String audit(Long ids[]){

        if(ids!=null && ids.length>0){
            for(Long id :ids){
                genHtml(id);
            }
        }
        return "success";
   }


    /**
     * 创建静态文件
     * @param id
     */
    private void genHtml(Long id) {

        try {
            //创建核心对象
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");

            Map<String ,Object> dateModel=new HashMap<>();

            //调用业务层查询数据
            Goods goods = goodsService.findItemByGoodsIdAndStatus(id, "1");
            dateModel.put("goods",goods.getGoods());
            //设置基本信息描述
            dateModel.put("goodsDesc",goods.getGoodsDesc());

            //设置sku集合
            dateModel.put("itemList",goods.getItemList());
            //设置一级分类名
            TbItemCat itemCat1 = itemCatService.findOne(goods.getGoods().getCategory1Id());
            dateModel.put("itemCat1",itemCat1.getName());

            //设置一级分类名
            TbItemCat itemCat2 = itemCatService.findOne(goods.getGoods().getCategory2Id());
            dateModel.put("itemCat2",itemCat2.getName());
            //设置一级分类名
            TbItemCat itemCat3 = itemCatService.findOne(goods.getGoods().getCategory3Id());
            dateModel.put("itemCat3",itemCat3.getName());

            //创建输出对象
            String fileName=ITEM_HTML_PATH+id+".html";
            FileWriter fileWriter=new FileWriter(fileName);
            //渲染页面
            template.process(dateModel,fileWriter);
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/delete")
    public String deletePage(Long ids[])throws Exception{
        for (Long id : ids) {
            String fileName=ITEM_HTML_PATH+id+".html";
            File file=new File(fileName);
            if(file.exists()){
                file.delete();
            }
        }
        return "success";
    }
}
