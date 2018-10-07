package cn.rumoss.ts.search;

import cn.rumoss.ts.util.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * SpringBoot启动类
 */
@SpringBootApplication
public class SearchApplication {

    public  static  void  main(String[]  args)  {
        SpringApplication.run(SearchApplication.class);
    }
    
     @Bean
    public  IdWorker  idWorker(){
        return  new  IdWorker(1,1);
    }

}
