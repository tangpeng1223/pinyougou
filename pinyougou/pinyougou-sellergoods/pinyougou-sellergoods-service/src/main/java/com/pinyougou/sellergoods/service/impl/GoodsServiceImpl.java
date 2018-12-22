package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.Goods;
import com.pinyougou.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Transactional
@Service(interfaceClass = GoodsService.class)
public class GoodsServiceImpl extends BaseServiceImpl<TbGoods> implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;//基本信息
    @Autowired
    private GoodsDescMapper goodsDescMapper; //描述
    @Autowired
    private SellerMapper sellerMapper; //商家

    @Autowired
    private BrandMapper brandMapper; //品牌

    @Autowired
    private ItemCatMapper itemCatMapper; //商品分类

    @Autowired
    private ItemMapper itemMapper; //商品

    /**
     * 分页查询
     * @param page 当前页
     * @param rows 每页显示的数据
     * @param goods 封装的查询条件
     * @return
     */
    @Override
    public PageResult search(Integer page, Integer rows, TbGoods goods) {
        PageHelper.startPage(page, rows);

        Example example = new Example(TbGoods.class);
        Example.Criteria criteria = example.createCriteria();
        //设置不查询商品的is_delete=1的商品
        criteria.andNotEqualTo("isDelete","1");

        //查询登录商家的商品信息
        if(!StringUtils.isEmpty(goods.getSellerId())){
            criteria.andEqualTo("sellerId", goods.getSellerId());
        }
        //根据状态查询
        if(!StringUtils.isEmpty(goods.getAuditStatus())){
            criteria.andEqualTo("auditStatus",goods.getAuditStatus());
        }
        //模糊查询商品
        if(!StringUtils.isEmpty(goods.getGoodsName())){
            criteria.andLike("goodsName", "%" + goods.getGoodsName() + "%");
        }

        List<TbGoods> list = goodsMapper.selectByExample(example);
        PageInfo<TbGoods> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 添加商品
     * @param goods 封装的实体goods
     */
    @Override
    public void add(Goods goods) {
        //新增商品信息
        goodsMapper.insert(goods.getGoods());

        //int a=10/0;
        //新增商品描述信息
        //设置商品描述的goods_id
        goods.getGoodsDesc().setGoodsId(goods.getGoods().getId());
        goodsDescMapper.insertSelective(goods.getGoodsDesc());
        //新增sku
        saveGoods(goods);

    }

    /**
     * 回显商品数据 通过商品id
     * @param id 商品id
     * @return  Goods（TbGoods GoodsDesc item）
     */
    public Goods findGoodsById(Long id) {
        //实例化封装对象
        Goods goods=new Goods();
        TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
        //设置商品信息spu
        goods.setGoods(tbGoods);
        TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(id);
        //设置基本信息
        goods.setGoodsDesc(tbGoodsDesc);

        Example example=new Example(TbItem.class);
        example.createCriteria().andEqualTo("goodsId",id);

        List<TbItem> tbItems = itemMapper.selectByExample(example);
        goods.setItemList(tbItems);
        //返回查询结果
        return goods;
    }

    /**
     * 保存修改的商品信息
     * @param goods 封装的商品数据
     */
    @Override
    public void updateGoods(Goods goods) {
        //修改商品状态为未审核
        goods.getGoods().setAuditStatus("0");
        goodsMapper.updateByPrimaryKeySelective(goods.getGoods());
        //修改描述信息
        goodsDescMapper.updateByPrimaryKeySelective(goods.getGoodsDesc());
        //删除原有的sku
        TbItem item=new TbItem();
        //设置删除id
        item.setGoodsId(goods.getGoods().getId());
        itemMapper.delete(item);
        //保存sku
        saveGoods(goods);

    }

    /**
     * 保存商品sku列表
     * @param goods 商品信息（基本、描述、sku列表）
     */
    private void saveGoods(Goods goods) {
        //判断是否启用了规格
        if("1".equals(goods.getGoods().getIsEnableSpec())){

            //启用了规格
            for(TbItem item:goods.getItemList()) {

                //组合规格选项形成 SKU 标题
                String title = goods.getGoods().getGoodsName();
                Map<String, Object> map = JSON.parseObject(item.getSpec());
                Set<Map.Entry<String, Object>> entries = map.entrySet();
                for (Map.Entry<String, Object> entry : entries) {
                    title += " " + entry.getValue().toString();
                }
                //设置标题
                item.setTitle(title);
                setItemValue(goods, item);
                //保存商品sku
                itemMapper.insertSelective(item);

            }

        }else{
            TbItem item=new TbItem();
            //设置标题
            item.setTitle(goods.getGoods().getGoodsName());
            //设置价格
            item.setPrice(goods.getGoods().getPrice());
            //设置库存
            item.setNum(9999);
            //设置默认
            item.setIsDefault("1");
            //设置为未审核
            item.setStatus("0");
            item.setSpec("{}");

            setItemValue(goods,item);

            //保存商品sku
            itemMapper.insertSelective(item);
        }
    }

    /**
     * 设置sku的值
     * @param goods 商品信息（基本、描述、sku列表）
     * @param item sku
     */
    private void setItemValue(Goods goods, TbItem item) {
        //设置图片
        List<Map> mapList = JSON.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);
        if (mapList.size() > 0 && mapList != null) {
            //将第一张图片作为sku的图片
            item.setImage(mapList.get(0).get("url").toString());
        }
        //商品分类 id
        item.setCategoryid(goods.getGoods().getCategory3Id());
        //商品分类名称
        TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(goods.getGoods().getCategory3Id());
        item.setCategory(itemCat.getName());
        //创建时间
        item.setCreateTime(new Date());
        //更新时间
        item.setUpdateTime(item.getCreateTime());
        //spu 商品id
        item.setGoodsId(goods.getGoods().getId());
        //商家id
        item.setSellerId(goods.getGoods().getSellerId());
        //商家名称
        TbSeller tbSeller = sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId());
        item.setSeller(tbSeller.getName());
        //品牌名称
        TbBrand tbBrand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
        item.setBrand(tbBrand.getName());

    }

    /**
     * @param ids    提交审核商品的id
     * @param status 修改状态
     * @return Result 返回结果
     */
    @Override
    public void updateStatus(Long[] ids, String status) {

        TbGoods goods=new TbGoods();
        //修改商品的状态
        goods.setAuditStatus(status);

        Example example=new Example(TbGoods.class);
        example.createCriteria().andIn("id", Arrays.asList(ids));
        //更新数据
        goodsMapper.updateByExampleSelective(goods,example);
         //如果审核通过则将sku改为启用
        if("2".equals(status)){
            //设置更新内容
           TbItem tbItem=new TbItem();
           tbItem.setStatus("1");
           //更新条件
           Example example1=new Example(TbItem.class);
           example1.createCriteria().andIn("goodsId", Arrays.asList(ids));
           itemMapper.updateByExampleSelective(tbItem,example1);
        }

    }

    /**
     * 删除商品 本质修改is_delete该字段为1
     * @param ids 要删除的商品的id数组
     * @return Result 处理结果
     */
    @Override
    public void deleteGoodsByIds(Long[] ids) {
          //设置修改内容
          TbGoods tbGoods=new TbGoods();
          tbGoods.setIsDelete("1");

          //设置修改的条件
          Example example=new Example(TbGoods.class);
          example.createCriteria().andIn("id", Arrays.asList(ids));
          //灵活性更新数据
          goodsMapper.updateByExampleSelective(tbGoods,example);
    }
}
