package com.busanit501.boot_project.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Log4j2
public class SampleController {

    // 내부클래스 임시로 만들기 (원래는 분리해서 사용한다.)
    class SampleDTO {
        private String p1, p2, p3;

        public String getP1() {
            return p1;
        }
        public String getP2() {
            return p2;
        }
        public String getP3() {
            return p3;
        }

    }

    @GetMapping("/hello") // hello.html로 이동.
    public void hello(Model model) {
        log.info("SampleController클래스, /hello : hello");
        model.addAttribute("msg", "Hello World!");
    }

    @GetMapping("/ex/ex1") // ex폴더 내 ex1.html로 이동
    public void ex1(Model model) {
        // 화면에 전달할 샘플데이터 배열
        List<String> list = Arrays.asList("aaa", "bbb", "ccc", "ddd");
            // 서버 -> 화면으로 데이터 전달
        model.addAttribute("list", list);

    }

    @GetMapping("/ex/ex2")
    public void ex2(Model model) {
        log.info("SampleController클래스, /ex/ex2 확인중");
        // 서버에서 화면으로 데이터 전달하는 예시,
        // html 파일의 화면에서는 넘어온 데이터 확인하기.
            // 전달데이터 1
        List<String> strList = IntStream.range(1, 10).mapToObj(i -> "Data" +i)
                .collect(Collectors.toList());
        model.addAttribute("list", strList);
            // 전달데이터 2
        Map<String, String> map = new HashMap<>();
        map.put("a", "aaaa");
        map.put("b", "bbbb");
        model.addAttribute("map", map);
            // 전달데이터 3
        SampleDTO sampleDTO = new SampleDTO();
        sampleDTO.p1 = "p1 ---- ";
        sampleDTO.p2 = "p2 ----";
        sampleDTO.p3 = "p3 ----";
        model.addAttribute("dto", sampleDTO);
    }
    @GetMapping("/ex/ex3")
    public void ex3(Model model) {
        model.addAttribute("list", Arrays.asList("aaa", "bbb", "ccc", "ddd"));

    }


}
