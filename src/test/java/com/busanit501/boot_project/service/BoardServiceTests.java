package com.busanit501.boot_project.service;

import com.busanit501.boot_project.dto.BoardDTO;
import com.busanit501.boot_project.dto.PageRequestDTO;
import com.busanit501.boot_project.dto.PageResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class BoardServiceTests {
    @Autowired
    private BoardService boardService;

    // 회원가입
    @Test
    public void testRegister() {
        // 입력할 더미 데이터 , 준비물 준비하고,
        BoardDTO boardDTO = BoardDTO.builder()
                .title("서비스 작업 단위 테스트 중")
                .content("서비스 작업 단위 테스트 내용 작성중 ")
                .writer("이상용")
                .build();
        // 실제 서비스 이용해서, 외주 주기.
        boardService.register(boardDTO);
    }// 회원가입 //

    // 하나 조회하기
    @Test
    public void testReadOne() {
        // 실제 디비번호인 bno로 내용 조회하기.
    BoardDTO boardDTO = boardService.readOne(102L);
    log.info("서비스 단위테스트, 하나조회 .readOne" + boardDTO);
    }// 하나 조회하기  //

    // 수정하기
    @Test
    public void testModify() {
        // 수정할 실제 데이터 이용해보자. 102L;
        BoardDTO boardDTO = boardService.readOne(102L);
        boardDTO.setTitle("Modify 테스트, 제목부분");
        boardDTO.setContent("Modify 테스트, 내용부분");

        boardService.modify(boardDTO);

    }// 수정하기 //

    // 삭제하기
    @Test
    public void testDelete() {
        boardService.remove(102L); // 담을 데이터 필요없이 삭제만 하면되서.
    }// 삭제하기 //


    @Test
    public void testList() {
        // 화면으로부터 전달받은 1)검색정보, 2)페이징 정보로
//            더미데이터 만들기.
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("1")
                .page(1) // PageRequestDTO에서 this.page - 1로 해놔서. 즉 0페이지
                .size(10)
                .build();

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
            log.info("BoardServiceTests 에서 작업, responseDTO : " + responseDTO);
    }
}