package cn.rumoss.ts.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import cn.rumoss.ts.util.IdWorker;

/**
 * SpringBoot启动类
 */
@SpringBootApplication
public class BaseApplication {

    public  static  void  main(String[]  args)  {
        SpringApplication.run(BaseApplication.class);
    }
    
     @Bean
    public  IdWorker  idWorker(){
        return  new  IdWorker(1,1);
    }

}
