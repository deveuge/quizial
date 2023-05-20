package com.deveuge.quizial.controller;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.deveuge.quizial.integrations.GiphyResponse;
import com.deveuge.quizial.integrations.GiphyResponseElement;
import com.deveuge.quizial.util.Constants;
import com.deveuge.quizial.util.pagination.PageRender;

@Controller
@RequestMapping("/giphy")
public class GiphyController {
	
	private static final String QUERY_PARAM_API_KEY = "api_key";
	private static final String QUERY_PARAM_SEARCH_TERM = "q";
	private static final String QUERY_PARAM_SEARCH_LIMIT = "limit";
	private static final String QUERY_PARAM_SEARCH_OFFSET = "offset";
	private static final String QUERY_PARAM_SEARCH_RATING = "rating";

	@Value("${giphy.api.key}")
	private String giphyAPIKey;
	
	@Value("${giphy.search.endpoint}")
	private String searchEndpoint;
	
	@Value("${giphy.search.limit}")
	private int searchLimit;
	
	@Value("${giphy.search.rating}")
	private String searchRating;
	
	@Value("${giphy.pages.limit}")
	private int pagesLimit;
	
	@Autowired
	private RestTemplate restTemplate;

	/**
	 * Performs a new search of gifs. Called when the user makes a new search through the modal.
	 * @param model {@link Model} Container that holds the data of the application
	 * @param query {@link String} Query to be used for the search
	 * @return {@link String} Returns the modal fragment with the gifs resulting from the search
	 */
	@PostMapping(value = "/search")
	public String search(Model model, @RequestParam String query) {
		return searchInGiphy(model, query, Constants.RESULTS_FIRST_PAGE);
	}
	
	/**
	 * Shows a specific page of the current search results.
	 * @param model {@link Model} Container that holds the data of the application
	 * @param query {@link String} Query to be used for the search
	 * @param page {@link Integer} Results page to return
	 * @return {@link String} Returns the modal fragment showing the requested page results
	 */
	@GetMapping(value = "/search")
	public String searchPage(Model model, @RequestParam String query, @RequestParam(defaultValue = "0") int page) {
		return searchInGiphy(model, query, page);
		
	}
	
	/**
	 * Performs a call to the Search Endpoint of the Giphy API to build the corresponding fragment for the view.
	 * @param model {@link Model} Container that holds the data of the application
	 * @param query {@link String} Query to be used for the search
	 * @param page {@link Integer} Results page to return
	 * @return {@link String} Returns the modal fragment showing the requested page results
	 */
	private String searchInGiphy(Model model, String query, int page) {
		URI requestURI = getSearchRequestUri(query, page);
		ResponseEntity<GiphyResponse> response = restTemplate.exchange(requestURI, HttpMethod.GET, null, GiphyResponse.class);
		
		Pageable pageable = PageRequest.of(page, searchLimit);
		GiphyResponse giphyResponse = response.getBody();
		Page<GiphyResponseElement> results = new PageImpl<>(giphyResponse.getData(), pageable, getTotalResults(giphyResponse));
		PageRender<GiphyResponseElement> pageRender = new PageRender<>("/giphy/search?query=" +  encodeQueryParam(query), results);
		
		model.addAttribute("results", results);
		model.addAttribute("page", pageRender);
		return "fragments/giphy :: gifs-container";
	}
	
	/**
	 * Builds the required URI to make the Giphy's Search API call.
	 * @param searchTerm {@link String} Query to be used for the search
	 * @param page {@link Integer} Results page to return
	 * @return {@link URI} Giphy's Search API URI
	 */
	private URI getSearchRequestUri(String searchTerm, int page) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(searchEndpoint)
	        .queryParam(QUERY_PARAM_API_KEY, giphyAPIKey)
	        .queryParam(QUERY_PARAM_SEARCH_TERM, searchTerm)
	        .queryParam(QUERY_PARAM_SEARCH_LIMIT, searchLimit)
	        .queryParam(QUERY_PARAM_SEARCH_OFFSET, searchLimit * page)
	        .queryParam(QUERY_PARAM_SEARCH_RATING, searchRating);
		return builder.build().toUri();
	}
	
	/**
	 * Calculates the total number of results for pagination purposes.
	 * @param giphyResponse {@link GiphyResponse} Response received after calling to Giphy's API
	 * @return Total number of results
	 */
	private long getTotalResults(GiphyResponse giphyResponse) {
		return giphyResponse.getTotalResults() / searchLimit > pagesLimit
				? searchLimit * pagesLimit 
				: giphyResponse.getTotalResults();
	}
	
	/**
	 * Translates the query into application/x-www-form-urlencodedformat using UTF-8.
	 * @param query {@link String} Query to be used for the search
	 * @return {@link String} UTF-8 encoded query
	 */
	private String encodeQueryParam(String query) {
		try {
			return URLEncoder.encode(query, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			return query;
		}
	}
	
}
