package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.vo.Goods;
import com.pinyougou.vo.PageResult;
import com.pinyougou.vo.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Security;
import java.util.List;

@RequestMapping("/goods")
@RestController
public class GoodsController {

    @Reference
    private GoodsService goodsService;

    @RequestMapping("/findAll")
    public List<TbGoods> findAll() {
        return goodsService.findAll();
    }

    @GetMapping("/findPage")
    public PageResult findPage(@RequestParam(value = "page", defaultValue = "1")Integer page,
                               @RequestParam(value = "rows", defaultValue = "10")Integer rows) {
        return goodsService.findPage(page, rows);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Goods goods) {
        try {
            //给商品添加商家名
            String sellerName= SecurityContextHolder.getContext().getAuthentication().getName();
            goods.getGoods().setSellerId(sellerName);
            //设置是否删除 默认为0
            goods.getGoods().setIsDelete("0");
            //设置商品是未审核的
            goods.getGoods().setAuditStatus("0");
            goodsService.add(goods);
            return Result.Success("增加成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("增加失败");
    }

    /**
     * 回显商品数据 通过商品id
     * @param id 商品id
     * @return  Goods（TbGoods GoodsDesc item）
     */
    @GetMapping("/findOne")
    public Goods findOne(Long id) {
        return goodsService.findGoodsById(id);
    }

    @GetMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            goodsService.deleteByIds(ids);
            return Result.Success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("删除失败");
    }

    /**
     * 分页查询列表
     * @param goods 查询条件
     * @param page 页号
     * @param rows 每页大小
     * @return
     */
    @PostMapping("/search")
    public PageResult search(@RequestBody  TbGoods goods, @RequestParam(value = "page", defaultValue = "1")Integer page,
                               @RequestParam(value = "rows", defaultValue = "10")Integer rows) {

        //获取用户登录名
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        goods.setSellerId(sellerId);
        return goodsService.search(page, rows, goods);
    }

    /**
     * 保存更新的商品数据
     * @param goods 封装的商品数据
     */
    @PostMapping("/update")
    public Result updateGoods(@RequestBody Goods goods){
        try {
            TbGoods oldGoods = goodsService.findOne(goods.getGoods().getId());
            String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
            //判断是否是同一商家
            if(!sellerId.equals(oldGoods.getSellerId())&& sellerId.equals(goods.getGoods().getSellerId())){
                   return Result.fail("操作非法");
            }
            //保存更新的数据
            goodsService.updateGoods(goods);
            return  Result.Success("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("修改失败");
    }

    /**
     * @param ids 提交审核商品的id
     * @param status 修改状态
     * @return Result 返回结果
     */
    @GetMapping("/updateStatus")
    public Result updateStatus(Long ids[],String status){
        try {
            goodsService.updateStatus(ids,status);
            return  Result.Success("提交审核成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  Result.fail("提交审核失败");
    }
}
