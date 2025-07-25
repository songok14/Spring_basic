package com.beyond.basic.b2_board.author.controller;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dto.*;
import com.beyond.basic.b2_board.author.service.AuthorService;
import com.beyond.basic.b2_board.common.CommonDto;
import com.beyond.basic.b2_board.common.CommonErrorDto;
import com.beyond.basic.b2_board.common.JwtTokenFilter;
import com.beyond.basic.b2_board.common.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.NoSuchElementException;

@RestController // Controller + ResponseBody
@RequiredArgsConstructor
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입
    @PostMapping("/create")
    // Dto에 있는 validation 어노테이션과 controller @Valid 한쌍
    /*아래 코드 포스트맨 테스트 데이터 예시
    * 1. multipart-formdata 선택
    * 2. authorCreateDto를 text로 {"name": "hong4","email": "hong4@naver.com","password": "12341234"}로
    *    세팅하면서 content-type을 application/json으로 설정
    * 3. profileImage는 gile로 세팅하면서 content-type을 multipart/form-data 설정
    * */


    public ResponseEntity<?> save(@RequestPart(name = "authorCreateDto") @Valid AuthorCreateDto authorCreateDto,
                                  @RequestPart(name = "profileImage", required = false) MultipartFile profileImage) {
        System.out.println(profileImage.getOriginalFilename());
//        try {
//            this.authorService.save(authorCreateDto);
//            return new ResponseEntity<>("OK", HttpStatus.CREATED);
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//            // 생성자 매개변수 body 부분의 객체와 header부의 상태코드 기입
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
        // ControllerAdvice가 없었으면 위와 같이 개별적인 예외처리가 필요하나 이제는 전역적인 예외처리가 가능
        this.authorService.save(authorCreateDto, profileImage);
        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }

    // 회원 목록조회
    @GetMapping("/list")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AuthorListDto> findAll() {
        return authorService.findAll();
    }

    @GetMapping("/myinfo")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> myInfo() {
        return new ResponseEntity<>(new CommonDto(authorService.myInfo(), HttpStatus.OK.value(), "마이페이지"), HttpStatus.OK);
    }


    // 회원 상세조회: id로 조회
    // 서버에서 별도의 try catch를 하지 않으면, 에러 발생 시 500에러 + 스프링의 포맷으로 에러 리턴
    @GetMapping("/detail/{id}")
    // ADMIN 권한이 있는지를 authentication 객체에서 쉽게 확인
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(new CommonDto(authorService.findById(id), HttpStatus.CREATED.value(), "ok"), HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(new CommonErrorDto(HttpStatus.NOT_FOUND.value(), "Fail such id"), HttpStatus.NOT_FOUND);
        }
    }

    // 비밀번호 수정: /author/updatepw email, password -> json
    // get: 조회, post: 등록, patch: 부분수정, put: 대체, delete: 삭제
    @PatchMapping("/updatepw")
    public ResponseEntity<?> updatePw(@RequestBody AuthorUpdatePwDto authorUpdatePwDto) {
        try {
            authorService.updatePassword(authorUpdatePwDto);
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // 회원 탈퇴(삭제): /author/delete/1
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        authorService.delete(id);
        return "OK";
    }

    @PostMapping("/dologin")
    public ResponseEntity<?> login(@RequestBody AuthorLoginDto authorLoginDto) {
        Author author = authorService.login(authorLoginDto);
        // 토큰 생성 및 return
        String token = jwtTokenProvider.createAtToken(author);
        return new ResponseEntity<>(new CommonDto(token, HttpStatus.OK.value(), "token is created"), HttpStatus.OK);
    }
}
