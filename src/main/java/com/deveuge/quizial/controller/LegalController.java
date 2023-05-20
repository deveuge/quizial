package com.deveuge.quizial.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.deveuge.quizial.util.ViewUtils;

@Controller
@RequestMapping("/legal")
public class LegalController {

	@GetMapping("/tou")
	public String termsOfUse(HttpServletRequest request) {
		Locale locale = ViewUtils.getLocale(request);
		return "legal/termsOfUse" + (locale.equals(ViewUtils.LOCALE_ES) ? "_es" : "");
	}
	
	@GetMapping("/pp")
	public String privacyPolicy(HttpServletRequest request) {
		Locale locale = ViewUtils.getLocale(request);
		return "legal/privacyPolicy" + (locale.equals(ViewUtils.LOCALE_ES) ? "_es" : "");
	}
	
}
