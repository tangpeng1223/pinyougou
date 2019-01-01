package com.pinyougou.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.sellergoods.service.ItemCatService;
import com.pinyougou.vo.Goods;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

/**
 * Date:2018/12/28
 * Author 唐鹏
 * DESC: 处理商品的详情页面
 */
@Controller
public class ItemController {

    @Reference
    private GoodsService goodsService;

    @Reference
    private ItemCatService itemCatService;
    @GetMapping("/{goodsId}")
    public ModelAndView toItemPage(@PathVariable Long goodsId){
        ModelAndView modelAndView=new ModelAndView("item");

        //据商品的spu id查询商品信息（基本、描述、已启用的sku列表）
        Goods goods=goodsService.findItemByGoodsIdAndStatus(goodsId,"1");
        //设置商品的基本信息
        modelAndView.addObject("goods",goods.getGoods());
        //设置描述信息
        modelAndView.addObject("goodsDesc",goods.getGoodsDesc());
        //设置sku集合
        modelAndView.addObject("itemList",goods.getItemList());
        //设置一级分类
        Long catrId=goods.getGoods().getCategory1Id();
        TbItemCat itemCat1 = itemCatService.findOne(catrId);
        modelAndView.addObject("itemCat1",itemCat1.getName());
        //设置二级分类
        TbItemCat itemCat2 = itemCatService.findOne(goods.getGoods().getCategory2Id());
        modelAndView.addObject("itemCat2",itemCat2.getName());
        //设置三级分类
        TbItemCat itemCat3 = itemCatService.findOne(goods.getGoods().getCategory3Id());
        modelAndView.addObject("itemCat3",itemCat3.getName());

        //返回处理结果
        return modelAndView;
    }
}
