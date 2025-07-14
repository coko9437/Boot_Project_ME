package com.busanit501.boot_project.repository;

import com.busanit501.boot_project.domain.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReplyRepository extends JpaRepository<Reply,Long> {
    @Query("select r from Reply r where r.board.bno = :bno")
                                //- r.board는 Reply 엔티티가 참조하고 있는 Board 엔티티
                                //- r.board.bno는 Board의 기본키(bno)
    Page<Reply> listOfBoard(Long bno, Pageable pageable);

}
