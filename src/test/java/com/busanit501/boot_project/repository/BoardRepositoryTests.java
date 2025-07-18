package com.busanit501.boot_project.repository;

import com.busanit501.boot_project.domain.Board;
import com.busanit501.boot_project.domain.BoardImage;
import com.busanit501.boot_project.dto.BoardListReplyCountDTO;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    // 추가-- 게시글 삭제시 댓글도 같이 삭제
    @Autowired
    private ReplyRepository replyRepository;


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
//====================== JpaRepository에서 확인 하는 부분은 여기==================================
            Board result = boardRepository.save(board);
                // .save : DB에 반영하기 => insert sql 문장과 결과 동일. = 쿼리메서드임.
//====================== JpaRepository에서 확인 하는 부분은 여기==================================
            log.info("bno : " +result.getBno());


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
//====================== JpaRepository에서 확인 하는 부분은 여기==================================
        // DB로 부터 조회된 데이터를 임시 객체에 담기.
        Optional<Board> result = boardRepository.findById(bno);
//====================== JpaRepository에서 확인 하는 부분은 여기==================================
        // 패턴숙지 필요. 있으면 엔티티클래스 받기.
        Board board = result.orElseThrow();
        // 변경할 제목, 내용 교체
        board.changTitleContent("수정제목","수정, 오늘 점심 뭐 먹지?");
        // 실제 디비에 반영.
//====================== JpaRepository에서 확인 하는 부분은 여기==================================
        boardRepository.save(board);
//====================== JpaRepository에서 확인 하는 부분은 여기==================================

    } // 3 update //

    // 4. delete
    @Test
    public void testDelete() {
        Long bno = 1L;
//====================== JpaRepository에서 확인 하는 부분은 여기==================================
        boardRepository.deleteById(bno);
//====================== JpaRepository에서 확인 하는 부분은 여기==================================
    } // 4. delete //

    // 5. 페이징 테스트
    @Test
    public void testPaging() {
        // 1 페이지에서, bno 기준으로 내림차순.
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
                    // PageRequest.of(페이지번호, 사이즈크기, 정렬기준)
        // JpaRepository 이용해서 페이징처리 된 데이터 받기

//====================== JpaRepository에서 확인 하는 부분은 여기==================================
        Page<Board> result = boardRepository.findAll(pageable);
//====================== JpaRepository에서 확인 하는 부분은 여기==================================

        // 페이징 관련 기본정보를 출력
//        1) 전체 갯수, 2)전체페이지, 3) 현재페이지 번호
//            4) 보여줄 사이즈 크기(10개),
//            5) DB에서 페이징된 조회할 데이터 10개 - (voList형식으로 표현했던거)
        log.info("전체 갯수 : total count : " + result.getTotalElements());
        log.info("전체 페이지 : total pages : " + result.getTotalPages());
        log.info("현재 페이지 번호 : page number  : " + result.getNumber());
        log.info("보여줄 사이즈 크기 : page size  : " + result.getSize());
        // 임시 리스트 생성해서, 디비에서 전달 받은 데이터를 담아두기.
        List<Board> todoList = result.getContent();
        log.info("디비에서 페이징된 조회될 데이터 10개 : todoList  : ");
        todoList.forEach(board -> log.info(board));
    } // 5. 페이징 테스트 //

    // 6. QueryDSL 확인, 자바문법으로 SQL 전달하기.
    @Test
    public void testSearch() {
        // 화면에서 전달받을 페이징 정보, 더미데이터 필요함.
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
            // 준비물.. 백그라운드 동작과정 확인해보기.
        boardRepository.search(pageable);

    } // 6. QueryDSL 확인, 자바문법으로 SQL 전달하기. //

    // 7. QueryDSL 확인, 자바문법으로 SQL 전달하기 2.
    @Test
    public void testSearchAll() {
        //더미데이터 2개필요.
        // 1)페이징정보 | 2) 검색 정보
        // 검색정보 <- 더미데이터 만들기.
        // 화면의 체크박스에서... "작성자","내용","제목"다 체크했다 가정.
        String[] types = {"t", "c", "w"};
            // 검색어
        String keyword = "97";
            // 페이징 정보
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
            // 실제 DB 가져오기 작업.
// ====================== JpaRepository에서 확인 하는 부분은 여기==================================
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
//====================== JpaRepository에서 확인 하는 부분은 여기==================================

        // 단위테스트 실행해서 SQL 전달하는거 확인,  콘솔에서 SQL 출력 확인하기, 목적.
        // 자바 문법으로 -> sql 어떻게 전달을 하는지 여부를 확인, 관건. !!!
        // 결과 값, 콘솔에서 확인.
        log.info("전체 갯수 : total count : " + result.getTotalElements());
        log.info("전체 페이지 : total pages : " + result.getTotalPages());
        log.info("현재 페이지 번호 : page number  : " + result.getNumber());
        log.info("보여줄 사이즈 크기 : page size  : " + result.getSize());
        log.info("이전 페이지 유무 : " + result.hasPrevious());
        log.info("다음 페이지 유무 : " + result.hasNext());
        // 임시 리스트 생성해서, 디비에서 전달 받은 데이터를 담아두기.
        List<Board> todoList = result.getContent();
        log.info("디비에서 페이징된 조회될 데이터 10개 : todoList  : ");
        todoList.forEach(board -> log.info(board));
    } // 7. QueryDSL 확인, 자바문법으로 SQL 전달하기 2. //

    // 기존 , 보드 정보 4가지에 이어서, 추가로 댓글 갯수 추가한 형태
    @Test
    public void testSearchReplyCount() {
        // 검색시 사용할, 더미 데이터 준비물
        // 1)
        String[] types = {"t","c","w"};
        //검색어
        String keyword = "97";
        // 페이징 정보,
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());

        // 댓글 갯수가 포함된 데이터를 조회
        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);
        // 결과 값, 콘솔에서 확인.
        log.info("전체 갯수 : total count : " + result.getTotalElements());
        log.info("전체 페이지 : total pages : " + result.getTotalPages());
        log.info("현재 페이지 번호 : page number  : " + result.getNumber());
        log.info("보여줄 사이즈 크기 : page size  : " + result.getSize());
        log.info("이전 페이지 유무 : " + result.hasPrevious());
        log.info("다음 페이지 유무 : " + result.hasNext());
        // 임시 리스트 생성해서, 디비에서 전달 받은 데이터를 담아두기.
        List<BoardListReplyCountDTO> todoList = result.getContent();
        log.info("디비에서 페이징된 조회될 데이터 10개 : todoList  : ");
        todoList.forEach(board -> log.info(board));
    }

    // 첨부된 이미지(BoardImage) 포함해서 게시글(Board) 작성해보기.
    @Test
    public void testInsertWithImages() {
        //더미데이터 만들기
        Board board = Board.builder()
                .title("첨부이미지 추가한 게시글")
                .content("첨부파일 추가해서 게시글 작성 테스트임. ")
                .writer("이상용")
                .build();

        // 더미데이터2 - 첨부이미지
        for(int i=0; i<3; i++) {
            board.addImage(UUID.randomUUID().toString(), "file" + i+".jpg");
        }
        boardRepository.save(board);
    }

    // Lazy 로딩 확인해보기.
    // 게시글 하나 조회시, 여기에 첨부된 이미지들도 조회를 하는 부분,
    @Test
    @Transactional
    public void testReadWithImages() {
        // 실제로 존재하는 이미지가 있는 게시글 확인부터. (bno:107L)
//        Optional<Board> result = boardRepository.findById(107L);  <<-- 각각 조회했음.
        Optional<Board> result = boardRepository.findByIdWithImages(107L);
        Board board = result.orElseThrow();
            log.info("testReadWithImages, board 확인중. : " +board);
//            log.info("testReadWithImages, board의 이미지들 확인 : " + board.getImageSet());
        for(BoardImage boardImage : board.getImageSet()) {
            log.info("첨부된 BoardImage 확인 :" + boardImage);
                //보드레포지토리의 @EntityGraph로 쟁반에 담아서 출력. 1줄로 나오던거-> 3줄로.
        }
    }

    //첨부된 이미지를 수정해보기. 고아 객체들의 처리 유무에 확인
    @Test
    @Transactional
    @Commit
    public void testModifyImage() {
        // 게시글1 안에 첨부이미지(3개) img1.jpg/ img2.jpg/ img3.jpg
            // 수정 -> 게시글1에 첨부이미지 변경 ==> test1.jpg/ test2.jpg/ test3.jpg
        // 기존 첨부된 이미지 img1.jpg ...3개는??
        // 실제 DB에도 기록이 된 상태지만
            // 부모인 게시글1이 없어진상태 ... 첨부이미지? '고아객체'
            // 스프링에서 고아객체? 가비지 컬렉션이 알아서 자동수거하게..
        //실제 각자 DB에 있는 더미데이터로 확인해보기.

        Optional<Board> result = boardRepository.findByIdWithImages(107L);
        Board board = result.orElseThrow();

        // 기존 보드에 첨부된이미지들을 클리어,
            board.clearImages();
        // 새로 첨부된 이미지들로 교체
        for(int i=0; i<3; i++) {
            board.addImage(UUID.randomUUID().toString(), "Update-file_1" + i + ".jpg");
        }
        boardRepository.save(board);
    }

    @Test
    @Transactional
    @Commit
    public void testRemoveAll() {
        // 실제로 삭제할 DB (107L)
        replyRepository.deleteByBoard_Bno(107L);
        boardRepository.deleteById(107L);

    }
}
