package com.busanit501.boot_project.service;

import com.busanit501.boot_project.dto.PageRequestDTO;
import com.busanit501.boot_project.dto.PageResponseDTO;
import com.busanit501.boot_project.dto.ReplyDTO;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Log4j2
public class ReplyServiceTests {

    @Autowired
    private ReplyService replyService;

    @Test
    public void testRegister() {
        // 실제 디비에, 게시글이 존재해야함.
        ReplyDTO replyDTO = ReplyDTO.builder()
                .replyText("====샘플 서비스 테스트====, 댓글 작성1")
                .replyer("이상용")
                .bno(103L)
                .build();

        log.info("작성한 댓글 번호 : "+replyService.register(replyDTO));
    }

    //댓글찾기(rno번호로)
    @Test
    @Transactional
    public void testFindByRno() {
        //
        ReplyDTO replyDTO = replyService.read(6L);
            log.info("=====ReplyServiceTests====, 댓글 하나 조회" + replyDTO);

    }

    //수정하기
    @Test
    @Transactional
    @Rollback(false)
    public void testModify() {
        // 실제 DB를 조회해서 수정해보기.
            //찾으려는 댓글의 DTO를 먼저 조회.
        ReplyDTO replyDTO = replyService.read(6L);
//            그 댓글을 임시데이터로 만들고, 수정한뒤에
        replyDTO.setReplyText("=====서비스 단위테스트 중=====. 수정");
//            그 댓글을 다시 해당 rno번호에 저장시키기.
        replyService.modify(replyDTO);

    }

    //삭제하기
    @Test
    public void testDelete() {
        replyService.remove(4L);
    }

    // 페이징처리
    @Test
    public void testGetPaging() {
        // 페이징 처리를 위한 더미데이터 준비물 준비작업
//            1) 부모게시글 필요.
        Long bno = 104L;
        //페이징 정보 담기
        Pageable pageable = PageRequest.of(0, 10, Sort.by("rno").descending());
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
//                .keyword("")
                .build();
        PageResponseDTO<ReplyDTO> pageResponseDTO = replyService.getListofBoard(bno, pageRequestDTO);
            log.info("====ReplyRepositoryTests====, 페이징처리 확인 pageResponseDTO : " + pageResponseDTO);

    }
}
