package cn.rumoss.ts.qa;

import cn.rumoss.ts.util.IdWorker;
import cn.rumoss.ts.util.JwtUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * SpringBoot启动类
 */
@SpringBootApplication
@EnableDiscoveryClient //可以进行服务发现
@EnableFeignClients //开启Feign的客户端调用功能
public class QaApplication {

    public  static  void  main(String[]  args)  {
        SpringApplication.run(QaApplication.class);
    }
    
     @Bean
    public  IdWorker  idWorker(){
        return  new  IdWorker(1,1);
    }

    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil();
    }

}
