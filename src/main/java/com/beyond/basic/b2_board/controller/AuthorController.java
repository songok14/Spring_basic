package com.beyond.basic.b2_board.controller;

import com.beyond.basic.b2_board.dto.*;
import com.beyond.basic.b2_board.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController // Controller + ResponseBody
@RequiredArgsConstructor
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;

    // 회원가입
    @PostMapping("/create")
    public ResponseEntity<?> save(@RequestBody AuthorCreateDto authorCreateDto) {
//        try {
//            this.authorService.save(authorCreateDto);
//            return new ResponseEntity<>("OK", HttpStatus.CREATED);
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//            // 생성자 매개변수 body 부분의 객체와 header부의 상태코드 기입
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
        // ControllerAdvice가 없었으면 위와 같이 개별적인 예외처리가 필요하나 이제는 전역적인 예외처리가 가능
        this.authorService.save(authorCreateDto);
        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }

    // 회원 목록조회
    @GetMapping("/list")
    public List<AuthorListDto> findAll() {
        return authorService.findAll();
    }

    // 회원 상세조회: id로 조회
    // 서버에서 별도의 try catch를 하지 않으면, 에러 발생 시 500에러 + 스프링의 포맷으로 에러 리턴
    @GetMapping("/detail/{id}")
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
}
