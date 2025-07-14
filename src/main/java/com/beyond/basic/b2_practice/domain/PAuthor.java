package com.beyond.basic.b2_practice.domain;

import com.beyond.basic.b2_practice.repository.PAuthorMemoryRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PAuthor {
    private Long id;
    private String name;
    private String email;
    private String password;

    public PAuthor(String name, String email, String password){
        this.id = PAuthorMemoryRepository.id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
