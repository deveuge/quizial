package com.deveuge.quizial.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultId implements Serializable {
	
	private static final long serialVersionUID = -6055017154584624537L;

	@Column(name = "quiz_id")
    private Long quizId;
    
    @Column(name = "letter")
    private String letter;
}
