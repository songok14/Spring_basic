package com.beyond.basic.b2_practice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PAuthorDetailDto {
    private Long id;
    private String name;
    private String email;
}
