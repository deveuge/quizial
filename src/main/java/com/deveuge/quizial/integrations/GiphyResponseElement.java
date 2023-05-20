package com.deveuge.quizial.integrations;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName(value = "data")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GiphyResponseElement {

	private String title;
	private String url;
	
    @SuppressWarnings("unchecked")
	@JsonProperty("images")
    private void unpackNested(Map<String,Object> images) {
        Map<String,String> original = (Map<String,String>) images.get("original");
        this.url = original.get("url");
    }
}
