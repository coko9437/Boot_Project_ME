package com.busanit501.boot_project.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
// @Controller : 경로와 데이터 둘다 전달.

// 서버가 데이터만 전달함. http의 body에 데이터를 담아서 전달
@RestController
@Log4j2
public class SampleRestController {

    @GetMapping("/hiStr")
    public String[] histr() {
       log.info("SampleRestController클래스, 작업중");
       return new String[]{"aaa", "bb", "c"};
    }

}
