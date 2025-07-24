package com.beyond.basic.b2_board.post.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@NotEmpty
@AllArgsConstructor
@Data
@Builder
public class PostSearchDto {
    private String category;
    private String title;
}
