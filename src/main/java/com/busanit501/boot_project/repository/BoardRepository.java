package com.busanit501.boot_project.repository;

import com.busanit501.boot_project.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> { // primary Key의 타입이 뭔지
    // JpaRepository <-- CRUD가 다 들어가 있음.

}
