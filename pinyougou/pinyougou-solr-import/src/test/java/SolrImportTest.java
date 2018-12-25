import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.ItemMapper;
import com.pinyougou.pojo.TbItem;
import com.sun.deploy.panel.ITreeNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * Date:2018/12/24
 * Author 唐鹏
 * DESC: 将数据库中的启用商品导入到solr中
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-*.xml")
public class SolrImportTest {

    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private ItemMapper itemMapper;
    @Test
    public void test(){
        TbItem tbItem=new TbItem();
        //设置查询启用的商品
        tbItem.setStatus("1");
        List<TbItem> itemList = itemMapper.select(tbItem);
        //转化格式
        for (TbItem item : itemList) {
            //将字符格式转化为map格式
            Map map = JSON.parseObject(item.getSpec(), Map.class);
            item.setSpecMap(map);
        }

        //导入数据
        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();

    }
}
