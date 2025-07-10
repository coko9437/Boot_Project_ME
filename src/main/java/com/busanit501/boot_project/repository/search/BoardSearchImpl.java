package com.busanit501.boot_project.repository.search;

import com.busanit501.boot_project.domain.Board;
import com.busanit501.boot_project.domain.QBoard;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;


import java.util.List;

// 인터페이스이름 + Impl => 이름 규칙임. 동일하게 작성해야함.
    // QuerydslRepositorySupport : 부모클래스. QueryDSL 사용하기위한 도구 함
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

    public BoardSearchImpl() {
        super(Board.class); // 부모클래스에서 엔티티클래스 만 전달. 사용할 클래스로 지정
    }

    @Override
    public Page<Board> search(Pageable pageable) {
//        이걸 왜 하냐? 자바 문법으로만 SQL 명령어를 전달하는게 목적임.

        // QueryDSL 사용법...
        // 순서1
        // Q도메인 객체 : 엔티티 클래스인 Board를 동적 쿼리 작업 하기위해
            // 편하게 만든 클래스라고 생각하면됨.
            // 기능이 향상된 버전임.
        QBoard board = QBoard.board; // board 정의.

//-----  DB 테이블을 자바 객체로 표현하는 것이 Entity 클래스(Board.class) -----//

        // 순서2
        JPQLQuery<Board> query = from(board); // select * from board;

        // 순서3
        query.where(board.title.contains("1")); // <- where title like...

        // 순서4
        List<Board> list = query.fetch(); //fetch : 가져오기 | DB에서 데이터 가져옴

        // 순서5
        long count = query.fetchCount(); // 가져온 DB 데이터 갯수 확인.

        // 순서6
            // 페이징 조건 추가해보기. getQuerydsl 에 페이징조건 탑재
        this.getQuerydsl().applyPagination(pageable,query);

        // 순서7
            // 페이징 조건을 적용 후 조회하기
        List<Board> list2 = query.fetch();

        // 순서8
            // 페이징 조건적용 + 전체 갯수
        long count2 = query.fetchCount();

        return null;
    }

    // 페이징정보 + 검색정보를 이용해서
        // 자바 메서드로 SQL 전달하기.
    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) { // pageable : 몇개씩 보여줄건지?
        // 순서 1. 고정,
        QBoard board = QBoard.board; // board 정의.

        // 순서 2. 고정,
        JPQLQuery<Board> query = from(board); // select * from board;

        // 순서 3. 고정,
            // BooleanBuilder를 이용해서 조건 추가 (= where 조건절)
        if((types != null && types.length > 0 )&& keyword != null) {

            BooleanBuilder builder = new BooleanBuilder();
                // BooleanBuilder -> OR / AND 조건을 사용하기 쉽다.
            for(String type : types) {
                    // types : 배열임. { "t", "w", "c" }=> title, writer, content
                switch (type) {
                    case "t" : builder.or(board.title.contains(keyword));
                    break;

                    case "c" : builder.or(board.content.contains(keyword));
                    break;

                    case "w" : builder.or(board.writer.contains(keyword));
                    break;
                }
            }
            query.where(builder); // select * from board where like %keyword%;
        }

        query.where(board.bno.gt(0L)); // bno > 0 추가.

        // 순서 4. 고정, paging 조건 추가하기.
        this.getQuerydsl().applyPagination(pageable,query);

        // 순서 5. 고정, 위 검색조건... SQL문 전달하기, 데이터 가져오기.
        List<Board> list = query.fetch();

        // 순서 6. 고정, 데이터 가지고올때
        long count = query.fetchCount();

        // 순서 7. 리턴타입에 맞추기
        return new PageImpl<>(list,pageable,count);
    }
}
