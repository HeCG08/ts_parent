package cn.rumoss.ts.gathering;

import cn.rumoss.ts.util.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

/**
 * SpringBoot启动类
 */
@SpringBootApplication
@EnableCaching
public class GatheringApplication {

    public  static  void  main(String[]  args)  {
        SpringApplication.run(GatheringApplication.class);
    }
    
     @Bean
    public  IdWorker  idWorker(){
        return  new  IdWorker(1,1);
    }

}
