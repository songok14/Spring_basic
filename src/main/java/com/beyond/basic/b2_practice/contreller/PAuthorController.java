package com.beyond.basic.b2_practice.contreller;

import com.beyond.basic.b2_board.domain.Author;
import com.beyond.basic.b2_practice.dto.PAuthorCreateDto;
import com.beyond.basic.b2_practice.service.PAuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/practice/author")
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
    public List<Author> pAuthorList() {
        List<Author> pAuthorList = new ArrayList<>();

        return pAuthorList;
    }

}
