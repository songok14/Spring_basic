package com.beyond.basic.b2_practice.service;

import com.beyond.basic.b2_board.domain.Author;
import com.beyond.basic.b2_practice.domain.PAuthor;
import com.beyond.basic.b2_practice.dto.PAuthorCreateDto;
import com.beyond.basic.b2_practice.dto.PAuthorListDto;
import com.beyond.basic.b2_practice.repository.PAuthorMemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PAuthorService {
    private final PAuthorMemoryRepository pAuthorMemoryRepository;

    public void register(PAuthorCreateDto pAuthorCreateDto){
        pAuthorMemoryRepository.register(pAuthorCreateDto);
    }

    public List<PAuthorListDto> pFindAll(){
        List<PAuthorListDto> pAuthorListDto = new ArrayList<>();
        for (Author a : pAuthorMemoryRepository.pFindAll()){
            pAuthorListDto.add(new PAuthorListDto(a.getId(), a.getName(), a.getEmail()));
        }

        return pAuthorListDto;
    }

}
