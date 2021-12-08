package ru.oz.demostatemachine.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.oz.demostatemachine.app.rest.UserContextInterceptor;

@Slf4j
@SpringBootApplication
public class DemoStatemachineApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(DemoStatemachineApplication.class, args);
    }

    @Autowired
    UserContextInterceptor userContextInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userContextInterceptor).addPathPatterns("/**");
    }

}
