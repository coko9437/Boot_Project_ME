package com.busanit501.boot_project.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

// 엔티티 클래스를 이용해서 Maria DB에 테이블 생성.
@Entity
@Getter
@Builder
@AllArgsConstructor // 모든 매개변수 생성자
@NoArgsConstructor // 디폴트 매개변수
@ToString(exclude = "imageSet") // 콘솔에 출력할때 자동으로 출력해주게
public class Board extends BaseEntity {

    @Id // DB pk와 같은 역할
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;
    private String content;
    private String writer;

    // 연간관계 설정1
    @OneToMany(mappedBy = "board", cascade = {CascadeType.ALL} // cascade = {CascadeType.ALL} : 특정 엔티티를 영속 상태로 만들 때,
    , fetch = FetchType.LAZY, orphanRemoval = true) // <- BoardImage의 board 변수를 가리킴 즉 외래키.
                                        // 중간 테이블 없이 연간관계 설정할 수 있음.
                                        // One : Board , Many : ImageSet
        // orphanRemoval : 고아객체 삭제하기

    @Builder.Default
    private Set<BoardImage> imageSet  = new HashSet<>();

    public void addImage(String uuid, String fileName) {
        BoardImage boardImage = BoardImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .board(this)
                .ord(imageSet.size())
                .build();
        imageSet.add(boardImage);
    }

    public void clearImages() {
        // 부모테이블(Board) 를 null 하게되면 '고아객체'가 됨.
            // 즉 부모테이블을 삭제하면 자식테이블인 첨부이미지를 자동 삭제.
        imageSet.forEach(boardImage -> boardImage.changeBoard(null));
        this.imageSet.clear();
    }


    // 불변성으로, 데이터를 직접 수정하지않고,
    // 변경하려는 필드를 , 따로 메서드로 분리해서 작업함.
    public void changTitleContent(String title, String content) {
        this.title = title;
        this.content = content;
    }
}

