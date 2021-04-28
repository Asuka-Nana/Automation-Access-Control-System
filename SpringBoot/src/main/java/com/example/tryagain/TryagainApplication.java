package com.example.tryagain;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.tryagain.mapper")
public class TryagainApplication {

    public static void main(String[] args) {
        SpringApplication.run(TryagainApplication.class, args);
    }

}
