package com.beyond.basic.b2_board.controller;

import com.beyond.basic.b2_board.dto.AuthorCreateDto;
import com.beyond.basic.b2_board.dto.AuthorDetailDto;
import com.beyond.basic.b2_board.dto.AuthorListDto;
import com.beyond.basic.b2_board.dto.AuthorUpdatePwDto;
import com.beyond.basic.b2_board.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Controller + ResponseBody
@RequiredArgsConstructor
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;

    // 회원가입
    @PostMapping("/create")
    public String save(@RequestBody AuthorCreateDto authorCreateDto) {
        try {
            this.authorService.save(authorCreateDto);
            return "OK";
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    // 회원 목록조회
    @GetMapping("/list")
    public List<AuthorListDto> findAll() {
        return authorService.findAll();
    }

    // 회원 상세조회: id로 조회
    // 서버에서 별도의 try catch를 하지 않으면, 에러 발생 시 500에러 + 스프링의 포맷으로 에러 리턴
    @GetMapping("/detail/{id}")
    public AuthorDetailDto findById(@PathVariable Long id) {
        try {
            return authorService.findById(id);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 비밀번호 수정: /author/updatepw email, password -> json
    // get: 조회, post: 등록, patch: 부분수정, put: 대체, delete: 삭제
    @PatchMapping("/updatepw")
    public String updatePw(@RequestBody AuthorUpdatePwDto authorUpdatePwDto) {
        authorService.updatePassword(authorUpdatePwDto);
        return "OK";
    }

    // 회원 탈퇴(삭제): /author/delete/1
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        authorService.delete(id);
        return "OK";
    }
}
