package com.kojikoji.gpt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.util.function.ToIntBiFunction;

/**
 * @ClassName Application
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/10/25 15:57
 * @Version
 */

@Slf4j
@RestController
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
