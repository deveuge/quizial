package com.deveuge.quizial.integrations;

import java.util.ArrayList;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GiphyResponse {

	private ArrayList<GiphyResponseElement> data;
	private int totalResults;
	private int currentResults;
	
	@JsonProperty("pagination")
    private void unpackNested(Map<String, Integer> pagination) {
        this.totalResults = pagination.get("total_count");
        this.currentResults = pagination.get("count");
    }
}
