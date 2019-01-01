import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.FileWriter;
import java.util.*;

/**
 * Date:2018/12/26
 * Author
 * DESC:
 */
public class FreemarkTest {

    @Test
    public void test()throws Exception{
        //创建配置对象
        Configuration configuration=new Configuration(Configuration.VERSION_2_3_23);

        //设置默认编码
        configuration.setDefaultEncoding("utf-8");
        //获取模板路劲
        configuration.setClassForTemplateLoading(FreemarkTest.class,"/ftl");

        //获取模板
        Template template = configuration.getTemplate("test.ftl");
        Map<String,Object> dataModel=new HashMap<>();
        dataModel.put("name","tangpeng");
        dataModel.put("message","你好欢迎来到这个世界");


        List<Map<String,Object>> goodsList=new ArrayList<>();
        Map<String,Object> goods1=new HashMap<>();
        goods1.put("name","香蕉");
        goods1.put("price","22.4");
        goodsList.add(goods1);

        Map<String,Object> goods2=new HashMap<>();
        goods2.put("name","苹果");
        goods2.put("price","123.2");
        goodsList.add(goods2);

        dataModel.put("goodsList",goodsList);
        dataModel.put("date",new Date());
        dataModel.put("number",1231231l);

        //输出路劲
        FileWriter fileWriter=new FileWriter("f:\\test\\test\\test.html");
        //渲染模板和数据
        template.process(dataModel,fileWriter);
        fileWriter.close();
    }

}
