package com.busanit501.boot_project.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
//@ToString(exclude = "board") // 해당 객체를 문자열 형식으로 출력
@Table(name = "Reply", indexes = {
        @Index(name = "idx_reply_board_bno", columnList = "board_bno")
}) // 인덱스 설정 : 성능,속도면에서 빠름
    // 해당 Reply 출력시, 부모테이블은 일단 제외
public class Reply extends BaseEntity{ //엔티티클래스 : DB와 직접 관계

    @Id // pk
    // 마리아 디비의 자동 순선 생성 정책을 따르게요.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    // Reply :N ---> :1 Board
    // 늦게 초기화 하겠다. 사용하는 시점에 로드하겠다
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    private String replyText; //댓글 내용
    private String replyer; // 댓글 작성자


}
