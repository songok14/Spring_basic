package com.beyond.basic.b2_board.author.domain;

import com.beyond.basic.b2_board.author.dto.AuthorDetailDto;
import com.beyond.basic.b2_board.author.dto.AuthorListDto;
import com.beyond.basic.b2_board.common.BaseTimeEntity;
import com.beyond.basic.b2_board.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
// JPA를 사용할 경우 entity에 반드시 붙여야 하는 어노테이션
// JPA의 EntityManager에게 객체를 위임하기 위한 어노테이션
// 엔티티 매니저는 영속성 컨텍스트(엔티티의 현재 상황, 맥락)를 통해 db 데이터 관리.
@Entity
//Builder 어노테이션을 통해 유연하게 객체 생성 가능하다.
@Builder
public class Author extends BaseTimeEntity {
    @Id // pk설정
    // identity: auto_increment, auto: id생성 전략을 jpa에게 자동설정하도록 위임
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 컬럼에 별다른 설정이 없을 경우 default varchar(255)
    // 컬럼명에 캐멀케이스 사용 시 db에는 created_time으로 컬럼 생성
    // @Column(name="pw") 되도록이면 컬럼명과 변수명을 일치시키는 것이 개발의 혼선을 줄일 수 있음
    private String name;
    @Column(length = 50, unique = true, nullable = false)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    @Builder.Default // 빌더패턴에서 변수 초기화(디폴트 값) 시 Builder.Default 어노테이션 필수
    private Role role = Role.USER;
    private String profileImgage;

    @OneToOne(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

    // OneToMany는 선택사항. 또한 default가 lazy
    // mappedBy에는 ManyToOne쪽에 변수명을 문자열로 지정
    // fk 관리를 반대편(post) 쪽에서 한다는 의미 -> 연관관계의 주인 설정 / fk의 주인
    // cascade: 부모 객체의 변화에 따라 자식 객체가 같이 변하는 옵션
    // persist: 저장
    // remove: 삭제
    // 자식의 자식까지 모두 삭제할 경우 orphanRemoval = ture 옵션 추가
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    List<Post> postList = new ArrayList<>();

    public void updatePw(String password) {
        this.password = password;
    }

//    public AuthorDetailDto detailFromEntity() {
//        return new AuthorDetailDto(this.id, this.name, this.email);
//    }

    public AuthorListDto listFromEntity() {
        return new AuthorListDto(this.id, this.name, this.email);
    }

    public void updateImageUrl(String imgUrl) {
        this.profileImgage = imgUrl;
    }
}
