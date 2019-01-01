package cn.tp.sms;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Date:2019/1/1
 * Author
 * DESC: springBoot引导类
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication springApplication=new SpringApplication(Application.class);
        //取消启动时加载横幅
        springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.run(args);
    }

}
