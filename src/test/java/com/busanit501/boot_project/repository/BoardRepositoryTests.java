package com.busanit501.boot_project.repository;

import com.busanit501.boot_project.domain.Board;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    // JpaRepository를 이용해서 기본CRUD 확인하기
        // SQL을 몰라도 자바의 메서드만 호출해서 SQL 전달하기.

    // 1 insert 확인.
    @Test
    public void testInsert() {
        // 더미데이터 병렬처리로 100개정도 하드코딩
        IntStream.rangeClosed(1,100).forEach(i -> {
            // 엔티티 클래스 -> 임시 객체생성 (반복문)
            Board board = Board.builder()
                    .title("title___" + i)
                    .content("content___" + i)
                    .writer("writer___" + (i%10))
                    .build();
            Board result = boardRepository.save(board);
                // .save : DB에 반영하기
            //JpaRepository에서 확인하는 부분 ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
            log.info("bno : " +result.getBno());
            //JpaRepository에서 확인하는 부분 ㅡㅡㅡㅡㅡㅡㅡㅡㅡ

        });
    } // 1 insert //

    // 2 select 확인
    @Test
    public void testSelect() {
        Long tno = 100L;
        //====================== JpaRepository에서 확인 하는 부분은 여기==================================
        Optional<Board> result = boardRepository.findById(tno);
        //====================== JpaRepository에서 확인 하는 부분은 여기==================================
        Board board = result.orElseThrow();
        log.info("bno : " + board);
    } // 2 select //

    // 3 update 확인
    @Test
    public void testUpdate() {
        Long bno = 100L;
        // DB로 부터 조회된 데이터를 임시 객체에 담기.
        Optional<Board> result = boardRepository.findById(bno);
        // 패턴숙지 필요. 있으면 엔티티클래스 받기.
        Board board = result.orElseThrow();
        // 변경할 제목, 내용 교체
        board.changTitleContent("수정제목","수정, 오늘 점심 뭐 먹지?");
        // 실제 디비에 반영.
        boardRepository.save(board);
    } // 3 update //

    // 4. delete
    @Test
    public void testDelete() {
        Long bno = 1L;
        boardRepository.deleteById(bno);
    }

    // 5. 페이징 테스트
    @Test
    public void testPaging() {
        // 1 페이지에서, bno 기준으로 내림차순.
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
        // JpaRepository 이용해서 페이징처리 된 데이터 받기
        Page<Board> result = boardRepository.findAll(pageable);

        // 페이징 관련 기본정보를 출력
//        1) 전체 갯수, 2)전체페이지, 3) 현재페이지 번호
//            4) 보여줄 사이즈 크기(10개),
//            5) DB에서 페이징된 조회할 데이터 10개 - (voList형식으로 표현했던거)
        log.info("전체 개수 : total count : " + result.getTotalElements());
        log.info("전체 페이지 : total page : " + result.getTotalPages());
        log.info("현재 페이지 번호 : page number : " + result.getNumber());
        log.info("보여줄 사이즈 크기 : page size : " + result.getSize());
        // 임시 리스트 생성해서 DB에서 전달받은 데이터 담기
        List<Board> todoList = result.getContent();
        log.info("DB에서 페이징 된 조회할 데이터 10개 : todoList : ");
        todoList.forEach(board -> log.info(board));

    }
}
