package com.busanit501.boot_project.service;


import com.busanit501.boot_project.domain.Board;
import com.busanit501.boot_project.dto.BoardDTO;
import com.busanit501.boot_project.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional() // <- 저장 하겠다.
public class BoardServiceImpl implements BoardService {
    // 화면에서 전달받은 DTO 데이터를, 엔티티 클래스 타입으로 변환해서
        // repository에게 외주주는업무.

    private final ModelMapper modelMapper; // <- 변환시키는 담당자
    private final BoardRepository boardRepository; // <- 실제 DB에 쓰기작업 하는 담당자

    @Override
    public Long register(BoardDTO boardDTO) {
        // 변환하자.
        Board board = modelMapper.map(boardDTO, Board.class);

        // 실제 DB에 쓰기작업 시작.
        Long bno = boardRepository.save(board).getBno();

        return bno;
    }

}
