package com.beyond.basic.b2_board.repository;

import com.beyond.basic.b2_board.domain.Author;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorMemoryRepository {
    private List<Author> authorList = new ArrayList<>();
    public static Long id = 1L;

    public void save(Author author){
        this.authorList.add(author);
        id++;
    }

    public List<Author> findAll(){
        return this.authorList;
    }

    public Optional<Author> findById(Long id){
        return authorList.stream().filter(a -> a.getId() == id).findFirst();
    }

    public Optional<Author> findByEmail(String email){
        return authorList.stream().filter(a -> a.getEmail().equals(email)).findFirst();
    }

    public void delete(Long id){
        int index = -1;
        for (int i=0; i<authorList.size(); i++){
           if (authorList.get(i).getId().equals(id)){
               index = i;
               break;
           }
        }
        authorList.remove(index);
    }
}
