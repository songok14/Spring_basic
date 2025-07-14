package com.beyond.basic.b2_practice.contreller;

import com.beyond.basic.b2_board.domain.Author;
import com.beyond.basic.b2_practice.domain.PAuthor;
import com.beyond.basic.b2_practice.dto.PAuthorCreateDto;
import com.beyond.basic.b2_practice.dto.PAuthorDetailDto;
import com.beyond.basic.b2_practice.dto.PAuthorListDto;
import com.beyond.basic.b2_practice.service.PAuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/practice")
public class PAuthorController {
    private final PAuthorService pAuthorService;

    // 회원가입
    @PostMapping("/register")
    public String register(@RequestBody PAuthorCreateDto pAuthorCreateDto) {
        pAuthorService.register(pAuthorCreateDto);
        return "OK";
    }

    // 회원 조회
    @GetMapping("/list")
    public List<PAuthorListDto> pAuthorList() {
        return pAuthorService.pFindAll();
    }

    // 회원 상세 조회
    @GetMapping("/detail/{id}")
    public PAuthorDetailDto detail(@PathVariable Long id){
        return pAuthorService.detail(id);
    }

    // 비밀번호 변경
    @PatchMapping("/updatepw")
    public String pUpdatePw(){

        return "OK";
    }

}
