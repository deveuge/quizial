package com.deveuge.quizial.util;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.deveuge.quizial.enums.Alert;

public class ViewUtils {
	
	public static final Locale LOCALE_ES = new Locale("es");
	
	private ViewUtils() {
		super();
	}
	
	public static void createAlert(RedirectAttributes redir, Alert alert, String message) {
		redir.addFlashAttribute(Constants.ALERT_CLASS, alert.name().toLowerCase());
		redir.addFlashAttribute(Constants.ALERT_MESSAGE, message);
	}
	
	public static Locale getLocale(HttpServletRequest request) {
		LocaleResolver resolver = RequestContextUtils.getLocaleResolver(request);
		return resolver == null ? Locale.ENGLISH : resolver.resolveLocale(request);
	}
}
