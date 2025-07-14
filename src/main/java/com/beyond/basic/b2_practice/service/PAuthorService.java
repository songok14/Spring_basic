package com.beyond.basic.b2_practice.service;

import com.beyond.basic.b2_practice.domain.PAuthor;
import com.beyond.basic.b2_practice.dto.PAuthorCreateDto;
import com.beyond.basic.b2_practice.dto.PAuthorDetailDto;
import com.beyond.basic.b2_practice.dto.PAuthorListDto;
import com.beyond.basic.b2_practice.repository.PAuthorMemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PAuthorService {
    private final PAuthorMemoryRepository pAuthorMemoryRepository;

    public void register(PAuthorCreateDto pAuthorCreateDto){
        PAuthor pAuthor = new PAuthor(pAuthorCreateDto.getName(), pAuthorCreateDto.getEmail(), pAuthorCreateDto.getPassword());
        pAuthorMemoryRepository.register(pAuthor);
    }

    public List<PAuthorListDto> pFindAll(){
        List<PAuthorListDto> pAuthorListDto = new ArrayList<>();
        for (PAuthor a : pAuthorMemoryRepository.pFindAll()){
            pAuthorListDto.add(new PAuthorListDto(a.getId(), a.getName(), a.getEmail()));
        }

        return pAuthorListDto;
    }

    public PAuthorDetailDto detail(Long id){
        PAuthor pAuthor = pAuthorMemoryRepository.detail(id).orElseThrow(() -> new NoSuchElementException());
        PAuthorDetailDto pAuthorDetailDto = new PAuthorDetailDto(pAuthor.getId(), pAuthor.getName(), pAuthor.getEmail());
        return pAuthorDetailDto;
    }

}
