package com.busanit501.boot_project.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardListReplyCountDTO { // 목적 : 서버에서 화면에 보여줄 데이터들을 담아둘 박스.
    // 게시글의 목록 표시 내용
    private Long bno;
    private String title;
    private String writer;
    private LocalDateTime regDate;

    // title옆에 댓글 갯수 표시하기
    private Long replyCount;


}
