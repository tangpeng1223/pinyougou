package com.pinyougou.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.sellergoods.service.SpecificationService;
import com.pinyougou.vo.PageResult;
import com.pinyougou.vo.Result;
import com.pinyougou.vo.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/specification")
@RestController
public class SpecificationController {

    @Reference
    private SpecificationService specificationService;


    /**
     * 通过id查询规格信息
     * @param id 规格id
     * @return Specification
     */
    @GetMapping("/findOne")
    public Specification findOne(Long id){

        return specificationService.findOne(id);
    }

    @RequestMapping("/findAll")
    public List<TbSpecification> findAll() {
        return specificationService.findAll();
    }

    @GetMapping("/findPage")
    public PageResult findPage(@RequestParam(value = "page", defaultValue = "1")Integer page,
                               @RequestParam(value = "rows", defaultValue = "10")Integer rows) {
        return specificationService.findPage(page, rows);
    }

    /**
     * {"specificationOptionList":[{"optionName":"白色","orders":"1"},{"optionName":"蓝色","orders":"2"}],
     *    "specification":{"specName":"颜色"}}
     * @param specification
     * @return Result 处理结果
     */
    @PostMapping("/add")
    public Result add(@RequestBody Specification specification) {
        try {
            specificationService.add(specification);
            return Result.Success("增加成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("增加失败");
    }


    /**
     * 更新规格数据
     * @param specification
     * @return  Result 处理结果
     */
    @PostMapping("/update")
    public Result update(@RequestBody Specification specification) {
        try {
            specificationService.update(specification);
            return Result.Success("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("修改失败");
    }

    @GetMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            specificationService.deleteSpecificationByIds(ids);
            return Result.Success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("删除失败");
    }

    /**
     * 分页查询列表
     * @param specification 查询条件
     * @param page 页号
     * @param rows 每页大小
     * @return
     */
    @PostMapping("/search")
    public PageResult search(@RequestBody  TbSpecification specification, @RequestParam(value = "page", defaultValue = "1")Integer page,
                               @RequestParam(value = "rows", defaultValue = "10")Integer rows) {
        return specificationService.search(page, rows, specification);
    }

}
