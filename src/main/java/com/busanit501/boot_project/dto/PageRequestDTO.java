package com.busanit501.boot_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.net.URLEncoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {
    // 화면 -> 서버로 요청
    @Builder.Default // 따로 요청 없으면 이걸 디폴트로 쓰겠다.
    private  int page = 1;

    @Builder.Default
    private  int size = 10;

    private String type; // 검색의 종류: t, c, w, tc, twc
    private String keyword; // 검색어
    private String link; // 쿼리스트링으로 검색어 직접 Get방식

    // 문자열 -> 문자열 요소로 가지는 배열
    public String[] getTypes() {
        if (type == null || type.isEmpty()) {
            return null;
        }
        return type.split(""); // 빈공간이면... twc -> t, w, c 이렇게 나눠줌.

    }

    // 페이징 정보 : 1) 현재페이지,
        // 2) 1페이지 당 보여줄 게시글 갯수, 3) 정렬조건, 내림차순
    public Pageable getPageable(String... props) {
                                // ...props : 가변인자임. (이름 or tno or title
        return PageRequest.of(this.page - 1, this.size, Sort.by(props).descending());
    }

    // 쿼리스트링
    //http://localhost:8080/todo/list
//     ?page=1
//     &size=10
//     &type=twc
//     &keyword=lsy
    // link = "page=1&size=10&type=twc&keyword=lsy"
    public String getLink() {
        if (link == null || link.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            builder.append("page="+ this.page);
            builder.append("&size="+ this.size);

            if (keyword != null && !keyword.isEmpty()) {
                try {
                    builder.append("&keyword=" + URLEncoder.encode(keyword, "UTF-8"));
                } catch (Exception e) {

                }
            }
            link = builder.toString();
        }
        return link;
    }
}
