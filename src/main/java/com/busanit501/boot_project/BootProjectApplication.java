package com.busanit501.boot_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // 부모 베이스 엔티티클래스에 설정된 Listener가 동작함.
public class BootProjectApplication { // 시작 메인 메서드임.

    public static void main(String[] args) {
        SpringApplication.run(BootProjectApplication.class, args);
    }

}
