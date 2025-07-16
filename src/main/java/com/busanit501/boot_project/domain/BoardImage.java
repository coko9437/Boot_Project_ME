package com.busanit501.boot_project.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter //불변성 유지... 그래서 Setter는 제외!
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "board") // 부모테이블 제외!
public class BoardImage implements Comparable<BoardImage>{
    // Comparable 테이블 : 정렬할때, 순서 정할때 사용함.
    @Id // 프라이머리 키
    private String uuid;
    private String fileName;
    private int ord;

    @ManyToOne // 부모(Board)테이블이 One
    private Board board;

    // 오름차순 또는 내림차순 정렬할 때 사용할 예정. compareTo
    @Override
    public int compareTo(BoardImage other) {
        return this.ord - other.ord;
    }

    public void changeBoard(Board board) {
        this.board = board;
    }

}
