package com.gym;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author gym
 */
// Scan the mapper package of the project
@MapperScan({"com.gym.project.monitor.mapper",
             "com.gym.project.system.mapper",
             "com.gym.project.tool.gen.mapper",
             "com.gym.project.gym.mapper",
             "com.gym.project.gymmaster.mapper"})
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class GymApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(GymApplication.class, args);
        System.out.println("启动成功!!!");
    }
}
