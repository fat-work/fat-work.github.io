package com.org;

import cn.hutool.core.io.resource.ClassPathResource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;

@SpringBootApplication
@EnableWebMvc
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

//        try {
//            openDefaultBrower();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 打开默认浏览器
     */
    private static void openDefaultBrower() throws IOException {
        ClassPathResource resource = new ClassPathResource("application.properties");
        Properties properties = new Properties();
        properties.load(resource.getStream());
        String port = properties.getProperty("server.port");
        try {
            InetAddress addr = InetAddress.getLocalHost();
            System.out.println("Local HostAddress: "+addr.getHostAddress());
            Runtime.getRuntime().exec("cmd   /c   start   http://"+addr.getHostAddress()+":"+port);//可以指定自己的路径
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

