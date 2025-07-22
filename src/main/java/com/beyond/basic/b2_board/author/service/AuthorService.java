package com.beyond.basic.b2_board.author.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dto.*;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service    // Component로도 대체 가능(트랜잭션 처리가 없는 경우)
@Transactional  // 스프링에서 메서드 단위로 트랜잭션(commit) 처리를 하고 만약 예외(unchecked) 발생 시 자동 롤백처리 지원
@RequiredArgsConstructor
public class AuthorService {
//    // 의존성주입: 객체를 갖다 쓰겠다.
//    // 의존성주입(DI) 방법 1. Autowired 어노테이션 사용 -> 필드 주입
//    // 런타임 시점에 초기화 되기때문에 final 사용 불가
//    @Autowired
//    private AuthorRepositoryInterface autorRepository;
//
//    // 의존성주입 방법 2. 생성자 주입방식(가장 많이 쓰는 방식)
//    // 장점: 1) final을 통해 상수로 사용 가능(안정성 향상) 2) 다형성 구현 가능 3) 순환참조방지(컴파일타임에 체크)
//    // 순환참조: R -> S -> C 순으로 생성되는데 S의 무언가를 R에서 필요로 할 때 발생(S -> R)
//    private final AuthorRepositoryInterface authorRepository;
//
//    // 객체로 만들어지는 시점에 스프링에서 AuthorRepository 객체를 매개변수로 주입
//    @Autowired // 생성자가 하나밖에 없을 경우 Autowired 생략 가능
//    public AuthorService(AuthorMemoryRepository authorMemoryRepository) {
//        this.authorRepository = authorMemoryRepository;
//    }

    // 의존성주입 방법 3. RequiredArgsConstructor 어노테이션 사용 -> 반드시 초기화 되어야 하는 필드(final 등)을 대상으로 생성자를 자동 생성
    // 다형성 설계는 불가
//    private final AuthorMemoryRepository authorMemoryRepository;
    private final AuthorRepository authorRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    // 객체 조립은 서비스 담당
    public void save(AuthorCreateDto authorCreateDto) {
        // 이메일 중복검증
        // this.autorRepository.save("..."); // DI 방법 1.
        // 비밀번호 길이 검증
        if (authorRepository.findByEmail(authorCreateDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

//        Author author = new Author(authorCreateDto.getName(), authorCreateDto.getEmail(), authorCreateDto.getPassword());
        // toEntity 패턴을 통해 Author객체 조립을 공통화
//        Author dbAuthor = this.authorRepository.save(author);
        String encodedPassword = passwordEncoder.encode(authorCreateDto.getPassword());
        Author author = authorCreateDto.authorToEntity(encodedPassword);

        // cascading 테스트: 회원이 생성될 때 곧바로 "가입인사" 글을 생성하는 상황, 방법 2가지
        // 방법 1. 직접 POST 객체 생성 후 저장
        Post post = Post.builder()
                .title("안녕하세요")
                .contents(authorCreateDto.getName() + "입니다. 반갑습니다.")
                // author 객체가 db에 save 되는 순간 엔티티매니저와 영속성 컨텍스트에 의해 author 객체에도 id 값 생성
                .author(author)
                .build();
//        postRepository.save(post);
        // 방법 2. cascade 옵션 활용, persist
        author.getPostList().add(post);
        authorRepository.save(author);
    }

    // 트랜잭션이 필요없는 경우 아래와 같이 명시적으로 제외
    @Transactional(readOnly = true)
    public List<AuthorListDto> findAll() {
//        List<AuthorListDto> authorListDtoList = new ArrayList<>();
//        for (Author a : authorMemoryRepository.findAll()) {
//            authorListDtoList.add(a.listFromEntity());
//        }
//        return authorListDtoList;
        return authorRepository.findAll().stream().map(a -> a.listFromEntity()).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AuthorDetailDto findById(Long id) throws NoSuchElementException {
        // 예외처리를 서비스에서 하기 때문에 Optional객체를 서비스에서 꺼냄
        // 스프링에서 예외는 롤백 기준
        Author author = authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));
        // 연관관계 설정 없이 조회해서 count 값 찾는 경우
//        List<Post> postListDtos = postRepository.findByAuthorId(id);
//        AuthorDetailDto authorDetailDto = AuthorDetailDto.fromEntity(author, postListDtos.size());

//        List<Post> postListDtos = postRepository.findByAuthor(author);
//        AuthorDetailDto authorDetailDto = AuthorDetailDto.fromEntity(author, postListDtos.size());

//        List<Post> postListDtos = postRepository.findByAuthor(author);
        AuthorDetailDto authorDetailDto = AuthorDetailDto.fromEntity(author);
        return authorDetailDto;
    }

    @Transactional(readOnly = true)
    public Author findByEmail(String email) throws NoSuchElementException {
        return authorRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));
    }

    public void updatePassword(AuthorUpdatePwDto authorUpdatePwDto) throws NoSuchElementException {
        // 객체를 수정한 후 별도의 update쿼리를 발생시키지 않아도 영속성 컨텍스트에 의해 객체 변경사항 자동 DB반영
        authorRepository.findByEmail(authorUpdatePwDto.getEmail()).orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다.")).updatePw(authorUpdatePwDto.getPassword());
    }

    public void delete(Long id) throws NoSuchElementException {
        // id 값으로 요소의 index 값을 찾아 삭제
        Author author = authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));
        authorRepository.delete(author);
    }

    public Author login(AuthorLoginDto authorLoginDto) {
        Optional<Author> optionalAuthor = authorRepository.findByEmail(authorLoginDto.getEmail());
        boolean check = true;
        if (!optionalAuthor.isPresent()) {
            check = false;
        } else if(!passwordEncoder.matches(authorLoginDto.getPassword(), optionalAuthor.get().getPassword())) {
            check = false;
        }

        if (!check) {
            throw new IllegalArgumentException("email 또는 비밀번호가 일치하지 않습니다.");
        }

        return optionalAuthor.get();
    }
}