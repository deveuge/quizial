package com.deveuge.quizial.view.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.deveuge.quizial.util.Constants;
import com.deveuge.quizial.util.SecurityUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class QuizDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 1000)
    private String description;
    
    @Size(max = 255)
    private String image;
    
    @NotNull
    private Long category;
    
	private MultipartFile imageFile;

	private Date createdAt;
	
	private Date updatedAt;

    private String user;
    private String userImage;
	
    @Size(min=1, max=10)
	private ArrayList<QuestionDTO> questions = new ArrayList<>();
    @Size(min=1, max=4)
	private ArrayList<ResultDTO> results = new ArrayList<>();

	public String getFullImage() {
		if(!StringUtils.hasText(image)) {
			return Constants.DEFAULT_IMAGE;
		}
		return image;
	}
	
	public String getTimeAgo(String locale) {
		PrettyTime p = new PrettyTime(new Locale(locale));
		return StringUtils.capitalize(p.format(updatedAt));
	}
	
	public boolean isUserOwner() {
		return SecurityUtils.getUsername().equalsIgnoreCase(this.user);
	}
	
	public void setChildIds() {
		AtomicLong questionsCounter = new AtomicLong(0);
		AtomicLong answersCounter = new AtomicLong(0);
		AtomicLong resultCounter = new AtomicLong(0);

		questions.forEach(question -> {
			question.setId(questionsCounter.incrementAndGet());
			question.getAnswers().forEach(answer -> answer.setId(answersCounter.incrementAndGet()));
		});
		results.forEach(result -> result.setId(resultCounter.incrementAndGet()));
	}
	
	public void cleanChildIds() {
		questions.forEach(question -> {
			question.setId(null);
			question.getAnswers().forEach(answer -> answer.setId(null));
		});
		results.forEach(result -> result.setId(null));
	}
	
}
