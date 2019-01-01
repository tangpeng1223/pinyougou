package cn.tp;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Date:2018/12/31
 * Author
 * DESC: 引导类
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
       // SpringApplication.run(Application.class,args);
        SpringApplication springApplication=new SpringApplication(Application.class);
        //启动时设置不出现横幅
        springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.run(args);
    }
}
