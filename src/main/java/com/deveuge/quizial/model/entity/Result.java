package com.deveuge.quizial.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Result implements Serializable {
	
	private static final long serialVersionUID = 8985910593643146327L;

	@EmbeddedId
	private ResultId id;

	@Column
	private String name;

	@Column(columnDefinition = "longtext")
	private String description;

	@Column
	private String image;
	
	@MapsId("quizId")
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;
	
}
