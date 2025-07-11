package com.busanit501.boot_project.controller.advice;

import lombok.extern.log4j.Log4j2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
public class CustomRestAdvice {

        //@ExceptionHandler : 어떤 예외를 처리할 지 종류를 지정.
    @ExceptionHandler(BindException.class) // 예외를 처리할 종류 알려주기.
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED) // 응답코드, 예외, 실패 메시지 코드 전달.
    public ResponseEntity<Map<String,String>> handleException(BindException e) {
        log.error("CustomRestAdvice 클래스에서, 레스트 처리시, 유효성 체크 작업중.",e);

        // errorMap : 예외 종류, 오류 메세지를 담을 임시 저장소(객체)
        Map<String, String> errorMap = new HashMap<>();
        if(e.hasErrors()){ // 댓글 작성중 오류가 있다면 맵에 담아서 전달할게.
            BindingResult bindingResult = e.getBindingResult(); // BindingResult : 오류모음집. 문제점꺼내서 맵에 담기.
            bindingResult.getFieldErrors().forEach((fieldError) -> {
                errorMap.put(fieldError.getField(),fieldError.getDefaultMessage());
            });
        }
        // 서버에서 화면으로 준비물을 전달함 1) 상태코드 400 badrequest : 잘못된 형식으로 보냈어.
//            2) body(errorMap); : 데이터 전달, 구체적인 오류의 내용들.
        return ResponseEntity.badRequest().body(errorMap);
    }
}
