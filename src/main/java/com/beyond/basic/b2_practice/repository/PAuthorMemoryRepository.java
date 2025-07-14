package com.beyond.basic.b2_practice.repository;

import com.beyond.basic.b2_board.domain.Author;
import com.beyond.basic.b2_practice.dto.PAuthorCreateDto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PAuthorMemoryRepository {
    private List<Author> authorList = new ArrayList<>();
    private static Long id = 1L;

    public void register(PAuthorCreateDto pAuthorCreateDto) {
        authorList.add(new Author(id, pAuthorCreateDto.getName(), pAuthorCreateDto.getEmail(), pAuthorCreateDto.getPassword()));
        id++;
    }

    public List<Author> pFindAll(){
        return authorList;
    }




}
