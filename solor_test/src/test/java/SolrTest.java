import com.pinyougou.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.ws.soap.Addressing;
import java.math.BigDecimal;

/**
 * Date:2018/12/24
 * Author
 * DESC:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-solr.xml")
public class SolrTest {

    @Autowired
    private SolrTemplate solrTemplate;

    //测试新增和更新
    @Test
    public  void testAdd(){
        TbItem item = new TbItem();
        item.setId(1000072674L);
        item.setTitle("222 一加手机6T 8GB+128GB 墨岩黑 唐鹏 光感屏幕指纹 全面屏双摄游戏手机 全网通4G 双卡双待");
        item.setPrice(new BigDecimal(3599));
        item.setImage("https://item.jd.com/100001172674.html");
        item.setBrand("一加");
        item.setCategory("手机");

        solrTemplate.saveBean(item);
        solrTemplate.commit();
    }

    /**
     * 根据id删除
     */
    @Test
    public  void delete(){
        solrTemplate.deleteById("2");
        solrTemplate.commit();
    }

    /**
     * 条件删除
     */
    @Test
    public void deleteByQuery(){
        Criteria criteria=new Criteria("item_title").is("唐鹏");
        SimpleQuery query=new SimpleQuery(criteria);
        solrTemplate.delete(query);
        solrTemplate.commit();
    }


    /**
     * 条件查询
     */
    @Test
    public void testQuery(){
        //设置查询条件
        Criteria criteria=new Criteria("item_title").contains("唐鹏");
        //创建查询
        SimpleQuery query=new SimpleQuery();
        query.addCriteria(criteria);
        //设置起始值
        query.setOffset(0);
        //设置每页显示的数据数
        query.setRows(10);
        ScoredPage<TbItem> tbItems = solrTemplate.queryForPage(query, TbItem.class);
        showPage(tbItems);
    }

    /**
     * 多条件查询
     */
    @Test
    public void testMutilQuery(){
        //设置查询条件
        Criteria criteria=new Criteria("item_title").contains("唐");
        Criteria criteria2=new Criteria("item_title").contains("手机");
        //创建查询
        SimpleQuery query=new SimpleQuery();
        query.addCriteria(criteria);
        query.addCriteria(criteria2);
        //设置起始值
        query.setOffset(0);
        //设置每页显示的数据数
        query.setRows(10);
        ScoredPage<TbItem> tbItems = solrTemplate.queryForPage(query, TbItem.class);
        showPage(tbItems);
    }

    private void showPage(ScoredPage<TbItem> tbItems) {
        System.out.println("总记录数"+tbItems.getTotalElements());
        System.out.println("总页数"+tbItems.getTotalPages());
        for (TbItem tbItem : tbItems) {
            System.out.println(tbItem.getBrand());
            System.out.println(tbItem.getTitle());
            System.out.println(tbItem.getCategory());
            System.out.println(tbItem.getId());

        }
    }


}
