package com.busanit501.boot_project.repository.search;

import com.busanit501.boot_project.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BoardSearch {
    // Page : 페이징하기 위한 정보를 담아두는 클래스 (페이징 전)
    // pageable : 페이징처리가 된 데이터를 담아둘 박스 (페이징 후)
    Page<Board> search(Pageable pageable);

    // 2 페이징 정보 에다가 + 검색정보까지 포함하기.
    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);
}
