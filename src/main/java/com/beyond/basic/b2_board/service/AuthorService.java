package com.beyond.basic.b2_board.service;

import com.beyond.basic.b2_board.domain.Author;
import com.beyond.basic.b2_board.dto.AuthorCreateDto;
import com.beyond.basic.b2_board.dto.AuthorDetailDto;
import com.beyond.basic.b2_board.dto.AuthorListDto;
import com.beyond.basic.b2_board.dto.AuthorUpdatePwDto;
import com.beyond.basic.b2_board.repository.AuthorMemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service    // Component로도 대체 가능(트랜잭션 처리가 없는 경우)

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
    private final AuthorMemoryRepository authorMemoryRepository;

    // 객체 조립은 서비스 담당
    public void save(AuthorCreateDto authorCreateDto) {
        // 이메일 중복검증
        // this.autorRepository.save("..."); // DI 방법 1.
        // 비밀번호 길이 검증
        if (authorMemoryRepository.findByEmail(authorCreateDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        Author author = new Author(authorCreateDto.getName(), authorCreateDto.getEmail(), authorCreateDto.getPassword());
        this.authorMemoryRepository.save(author);
    }

    public List<AuthorListDto> findAll() {
        List<AuthorListDto> authorListDtoList = new ArrayList<>();
        for (Author a : authorMemoryRepository.findAll()) {
            authorListDtoList.add(new AuthorListDto(a.getId(), a.getName(), a.getEmail()));
        }
        return authorListDtoList;
    }

    public AuthorDetailDto findById(Long id) throws NoSuchElementException {
        // 예외처리를 서비스에서 하기 때문에 Optional객체를 서비스에서 꺼냄
        // 스프링에서 예외는 롤백 기준
        Author author = authorMemoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));
        AuthorDetailDto authorDetailDto = new AuthorDetailDto(author.getId(), author.getName(), author.getEmail());
        return authorDetailDto;
    }

    public Author findByEmail(String email) throws NoSuchElementException {
        return authorMemoryRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));
    }

    public void updatePassword(AuthorUpdatePwDto authorUpdatePwDto) throws NoSuchElementException {
        authorMemoryRepository.findByEmail(authorUpdatePwDto.getEmail()).orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다.")).updatePw(authorUpdatePwDto.getPassword());
    }

    public void delete(Long id) throws NoSuchElementException{
        // id 값으로 요소의 index 값을 찾아 삭제
        authorMemoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));
        authorMemoryRepository.delete(id);
    }
}
