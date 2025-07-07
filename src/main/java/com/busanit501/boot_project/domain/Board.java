package com.busanit501.boot_project.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// 엔티티 클래스를 이용해서 Maria DB에 테이블 생성.
@Entity
public class Board {

    @Id // <- DB의 pk와 같은 역할
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;
    private String content;
    private String Writer;
}
