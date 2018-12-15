package com.pinyougou.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import com.pinyougou.vo.PageResult;
import com.pinyougou.vo.Result;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Date:2018/12/10
 * Author tangpeng
 * DESC: 品牌表现控制层
 */
@RequestMapping("/brand")
@RestController
public class BrandController {
    @Reference
    private BrandService brandService;


    /**
     * 查询品牌 格式为：
     * [{"id":1,"text":"联想"}{"id":11,"text":"诺基亚"}],
     **/
    @GetMapping("/selectOptionList")
    public List<Map<String, Object>> selectOptionList() {
        List<Map<String, Object>> maps = brandService.selectOptionList();
        return maps;
    }


    /**
     * 带搜索条件查询
     *
     * @param tbBrand 查询条件
     * @param page    当前页
     * @param rows    页大小数据
     * @return
     */
    @PostMapping("/search")
    public PageResult search(@RequestBody TbBrand tbBrand,
                             @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer rows) {
        PageResult search = brandService.search(tbBrand, page, rows);
        if (search.getTotal() < (page - 1) * rows) {
            return brandService.search(tbBrand, 1, rows);
        }
        return search;

    }

    /**
     * 批量删除数据
     *
     * @param ids 批量删除品牌的id
     * @return 返回处理结果
     */
    @GetMapping("/delete")
    public Result delete(Long ids[]) {
        try {
            //调用业务层批量删除
            brandService.deleteByIds(ids);
            return Result.Success("批量删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("批量删除失败");
    }

    /**
     * 更新品牌数据
     *
     * @param brand
     * @return Result
     */
    @PostMapping("update")
    public Result update(@RequestBody TbBrand brand) {

        try {
            brandService.update(brand);
            return Result.Success("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("修改失败");
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return TbBrand
     */
    @GetMapping("findOne")
    public TbBrand findOne(Long id) {
        TbBrand tbBrand = brandService.findOne(id);
        System.out.println("=====================================================" + tbBrand.getName());
        return tbBrand;
    }


    /**
     * 保存新增的品牌
     *
     * @param brand
     * @return Result
     */
    @PostMapping("/add")
    public Result add(@RequestBody TbBrand brand) {
        try {
            //保存添加信息
            brandService.add(brand);
            return Result.Success("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("保存品牌信息失败");
    }

    /**
     * 分页查询数据
     *
     * @param page 当前页
     * @param rows 每页显示的数据
     * @return PageResult 分页包装类
     */
    @GetMapping("/findPage")
    public PageResult findPage(@RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "10") Integer rows) {
        //分页查询
        return brandService.findPage(page, rows);
    }


    @GetMapping("findAll")
    public List<TbBrand> findAll() {
        return brandService.findAll();
    }

    @GetMapping("/testPage")
    public List<TbBrand> testPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer rows) {
        PageResult pageResult = brandService.findPage(page, rows);
        return (List<TbBrand>) pageResult.getRows();
    }
}
