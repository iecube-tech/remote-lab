package com.akehcloud.iecube;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.akehcloud")
@EnableJpaRepositories
@MapperScan(basePackages = "com.akehcloud.*.module.*.mapper")
@EnableAsync
@EnableTransactionManagement
public class ServiceIecubeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceIecubeApplication.class, args);
    }

}
