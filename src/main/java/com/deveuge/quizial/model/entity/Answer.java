package com.deveuge.quizial.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;

import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Answer implements Serializable {
	
	private static final long serialVersionUID = 865963037371049281L;

	@Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "result_letter")
    private String resultLetter;

    private static final String QUERY = "(SELECT r.id FROM result r WHERE r.letter = result_letter AND r.quiz_id = (SELECT q.quiz_id FROM question q WHERE q.id = question_id))";
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumnsOrFormulas ({
	    @JoinColumnOrFormula(formula = @JoinFormula(value = QUERY, referencedColumnName = "quiz_id")),
	    @JoinColumnOrFormula(column = @JoinColumn(name = "result_letter", referencedColumnName = "letter", insertable = false, updatable = false))
    })
    private Result result;
}
