package com.busanit501.boot_project.service;


import com.busanit501.boot_project.dto.BoardDTO;
import com.busanit501.boot_project.dto.BoardListReplyCountDTO;
import com.busanit501.boot_project.dto.PageRequestDTO;
import com.busanit501.boot_project.dto.PageResponseDTO;

public interface BoardService {
    // 추상클래스, 여기선 메소드만 쓰고 구현은 Impl에서...
    Long register(BoardDTO boardDTO);
    BoardDTO readOne(Long bno);
    void modify(BoardDTO boardDTO);
    void remove(Long bno);

    // 기존 1)페이징, 2) 검색
    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    // 변경 -> 1)페이징, 2)검색, 3)댓글갯수
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);

}
