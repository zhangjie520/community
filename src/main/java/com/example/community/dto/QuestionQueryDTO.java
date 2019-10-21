package com.example.community.dto;

import lombok.Data;

@Data
public class QuestionQueryDTO {
    private String search;
    private Integer pageIndex;
    private Integer size;
}
