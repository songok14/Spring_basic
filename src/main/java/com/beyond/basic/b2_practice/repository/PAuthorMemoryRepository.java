package com.beyond.basic.b2_practice.repository;

import com.beyond.basic.b2_board.domain.Author;
import com.beyond.basic.b2_practice.domain.PAuthor;
import com.beyond.basic.b2_practice.dto.PAuthorDetailDto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PAuthorMemoryRepository {
    private List<PAuthor> pAuthorList = new ArrayList<>();
    public static Long id = 1L;

    public void register(PAuthor pAuthor) {
        this.pAuthorList.add(pAuthor);
        id++;
    }

    public List<PAuthor> pFindAll() {
        return pAuthorList;
    }

    public Optional<PAuthor> detail(Long id) {
        return pAuthorList.stream().filter(a -> a.getId().equals(id)).findFirst();
    }


}
