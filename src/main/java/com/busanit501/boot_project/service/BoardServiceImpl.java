package com.busanit501.boot_project.service;


import com.busanit501.boot_project.domain.Board;
import com.busanit501.boot_project.dto.BoardDTO;
import com.busanit501.boot_project.dto.BoardListReplyCountDTO;
import com.busanit501.boot_project.dto.PageRequestDTO;
import com.busanit501.boot_project.dto.PageResponseDTO;
import com.busanit501.boot_project.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional() // <- 저장 하겠다.
public class BoardServiceImpl implements BoardService {
    // 화면에서 전달받은 DTO 데이터를, 엔티티 클래스 타입으로 변환해서
        // repository에게 외주주는업무.

    private final ModelMapper modelMapper; // <- 변환시키는 담당자
    private final BoardRepository boardRepository; // <- 실제 DB에 쓰기작업 하는 담당자

    // 회원가입.
    @Override
    public Long register(BoardDTO boardDTO) { //
        // 변환하자. VO -> DTO
        log.info("보드 서비스 구현체, 등록 과정 중에 변환된 boardDTO 확인 : " + boardDTO);
        Board board = modelMapper.map(boardDTO, Board.class);
        log.info("보드 서비스 구현체, 등록 과정 중에 변환된 board 확인 : " + board);

        // 실제 DB에 쓰기작업 시작.
        Long bno = boardRepository.save(board).getBno();

        return bno;
    }

    @Override
    public BoardDTO readOne(Long bno) {
        // 기능을 또 만들어서 구현하는게 아니라,
        // 다른 누군가가 만들어 둔 기능을 이용하기 = 외주주기
            // -> BoardRepository 에서 꺼내오자.
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();
        // 엔티티 클래스타입(VO) -> DTO 로 변환.
        BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);
        return boardDTO;
    }

    @Override
    public void modify(BoardDTO boardDTO) {
        // boardDTO : 화면에서 전달받은 수정할 데이터 정보가 들어있음.
            // 그 정보 중, bno 번호를 이용해서 기존 DB 불러오고,
                // 수정할 데이터로 교체하고 다시 DB에 저장하기.
        Optional<Board> result = boardRepository.findById(boardDTO.getBno());
        Board board = result.orElseThrow();
        board.changTitleContent(boardDTO.getTitle(), boardDTO.getContent());
        boardRepository.save(board);

    }

    @Override
    public void remove(Long bno) {
        boardRepository.deleteById(bno);
    }

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
        // 화면으로부터 전달받은 1)검색조건, 2)페이징 정보
        String[] types = pageRequestDTO.getTypes();
                //type = "twc" 정의 -> getTypes 가 -> { "t", "w", "c"}
                    // 변환한 걸 배열로 받아야하니까 String[]
        String keyword = pageRequestDTO.getKeyword(); // 검색어를 받아서
        Pageable pageable = pageRequestDTO.getPageable("bno");
            // 준비된 재료 : 서버에서 데이터(검색조건), 페이징 정보 가져오기.
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        // result 에 들어있는 정보들?
            // 전체 갯수, 전체페이지, 현재페이지번호, 보여줄사이즈크기.. (BoardRepositoryTests 에 있음)
        // 1)전체 갯수 2)전체 페이지 3) 현재 페이지 번호
        // 3) 보여줄 사이즈 크기 4) 이전 페이지 유무
        // 5) 다음 페이지 유무
        // 6) 페이징 처리된 실제 Board 내용도 있다.
        // 결과 값, 콘솔에서 확인.
//        log.info("전체 갯수 : total count : " + result.getTotalElements());
//        log.info("전체 페이지 : total pages : " + result.getTotalPages());
//        log.info("현재 페이지 번호 : page number  : " + result.getNumber());
//        log.info("보여줄 사이즈 크기 : page size  : " + result.getSize());
//        log.info("이전 페이지 유무 : " + result.hasPrevious());
//        log.info("다음 페이지 유무 : " + result.hasNext());




        // 페이징 처리된 실제 Board 내용... (Board -> BoardDTO 로 변환 -> 그걸 리스트로 변경.
        List<BoardDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board, BoardDTO.class))
                    .collect(Collectors.toList());

        //result.getContent() -> List<Board>
        //.stream().map : 리스트의 요소들을 각각 하나씩 순회 하면서, 타입 변환시키고, 전부다 순회
        //(board -> modelMapper.map(board, BoardDTO.class))
        //.collect(Collectors.toList()); : 변환된 DTO를 다시 리스트로 변환 하는 작업.
        // 병렬 처리, 빌더 패턴으로 한번 연쇄 적용하기.
        // 결과는,  BoardDTO 로 변환된 리스트로

        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
        // 이 메서드의 최종 반환 타입: PageResponseDTO<BoardDTO>
        // 1) 생성자 호출 2) 생성자를 특정 이름으로 정의 해둔 내용이 있다.
    }

    @Override
    public PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO) {
        // type = "twc" -> getTypes -> {"t","c","w"}
        // 화면으로 부터 전달 받은,
        // 1)검색 조건과 2)페이징 정보
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        //2) result : 보드 레포지토리 테스트에서, 관련 정보 확인 해주세요.
        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types,keyword,pageable);


        return PageResponseDTO.<BoardListReplyCountDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }


}
