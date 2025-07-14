package com.busanit501.boot_project.service;

import com.busanit501.boot_project.dto.PageRequestDTO;
import com.busanit501.boot_project.dto.PageResponseDTO;
import com.busanit501.boot_project.dto.ReplyDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;



public interface ReplyService {


    //댓글 등록하기
    Long register(ReplyDTO replyDTO);

    //댓글 (1개)조회
    ReplyDTO read(Long rno);

    //수정하기
    void modify(ReplyDTO replyDTO);

    //삭제하기
    void remove(Long rno);

    //페이징처리
    PageResponseDTO<ReplyDTO> getListofBoard(Long bno, PageRequestDTO pageRequestDTO);


}
