package com.deveuge.quizial.view.dto;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResultDTO {

    private Long id; // quizid
    
    private String resultLetter;

    @Size(max = 255)
    private String name;

    private String description;

    @Size(max = 255)
    private String image;
}

