package com.busanit501.boot_project.repository;

import com.busanit501.boot_project.domain.Board;
import com.busanit501.boot_project.repository.search.BoardSearch;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

// 설정한 인터페이스(BoardSearch)를 여기에 적용하기
public interface BoardRepository extends JpaRepository<Board, Long> , BoardSearch { // primary Key의 타입이 뭔지
    // JpaRepository <-- CRUD가 다 들어가 있음.
//    @Query(value = "select now()", nativeQuery = true)
//    String getTime();

        // 게시글 하나 조회할 때, 첨부된이미지를 각각 낱개로 조회함
            // -> 문제점 : DB가 피곤해 함.
            // @EntityGraph : 같이 Load 해야할 속성을 지정하기. 즉, 쟁반에 담아서 한꺼번에
    @EntityGraph(attributePaths = {"imageSet"})
    @Query("select b from Board b where b.bno = :bno")
    Optional<Board> findByIdWithImages(Long bno);
    //

    // 게시글 번호로 댓글 삭제하기.
    void deleteByBoard(Long bno);
}
