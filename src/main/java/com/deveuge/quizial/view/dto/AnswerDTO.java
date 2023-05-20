package com.deveuge.quizial.view.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AnswerDTO {

	private Long id;

    @Size(max = 255)
    private String name;

    @NotNull
    private String resultLetter;
	
}
