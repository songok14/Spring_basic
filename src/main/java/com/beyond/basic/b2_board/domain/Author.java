package com.beyond.basic.b2_board.domain;

import com.beyond.basic.b2_board.dto.AuthorDetailDto;
import com.beyond.basic.b2_board.dto.AuthorListDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
// JPA를 사용할 경우 entity에 반드시 붙여야 하는 어노테이션
// JPA의 EntityManager에게 객체를 위임하기 위한 어노테이션
// 엔티티 매니저는 영속성 컨텍스트(엔티티의 현재 상황, 맥락)를 통해 db 데이터 관리.
@Entity
public class Author {
    @Id // pk설정
    // identity: auto_increment, auto: id생성 전략을 jpa에게 자동설정하도록 위임
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 컬럼에 별다른 설정이 없을 경우 default varchar(255)

//    @Column(name="pw") 되도록이면 컬럼명과 변수명을 일치시키는 것이 개발의 혼선을 줄일 수 있음
    private String name;
    @Column(length = 50, unique = true, nullable = false)
    private String email;
    private String password;

    public Author(String name, String email, String password) {
//        this.id = AuthorMemoryRepository.id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void updatePw(String password) {
        this.password = password;
    }

    public AuthorDetailDto detailFromEntity() {
        return new AuthorDetailDto(this.id, this.name, this.email);
    }

    public AuthorListDto listFromEntity() {
        return new AuthorListDto(this.id, this.name, this.email);
    }
}
