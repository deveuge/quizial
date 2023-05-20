package com.deveuge.quizial.view.dto;

import com.deveuge.quizial.util.SecurityUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryDTO {

	private long id;
	private String descriptionENG;
	private String descriptionESP;
	
	public String getDescription() {
		return SecurityUtils.isSpanishLocale() ? descriptionESP : descriptionENG;
	}
}
