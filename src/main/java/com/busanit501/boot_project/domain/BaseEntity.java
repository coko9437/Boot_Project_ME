package com.busanit501.boot_project.domain;

// 모든 엔티티 테이블에 공통으로 들어가는 필드를 분리
// 공통으로 분리해서 사용함.

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class}) // 공유자원임, 전역에서 사용할 수 있음.
@Getter
abstract class BaseEntity { //추상클래스로 사용

    @CreatedDate // 생성할때 자동으로 필드가 추가됨
    @Column(name = "regdate", updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate // 마지막으로 수정되었을 경우 그 날짜를 쓰겠다.
    @Column(name = "modDate")
    private LocalDateTime modDate;

}
