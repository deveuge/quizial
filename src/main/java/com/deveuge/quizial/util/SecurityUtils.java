package com.deveuge.quizial.util;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.deveuge.quizial.model.entity.User;
import com.deveuge.quizial.security.UserPrincipalImpl;

public class SecurityUtils {

	private SecurityUtils() {
		super();
	}
	
	/**
	 * Check if the current user is authenticated
	 * @return {@link boolean} True if it's authenticated, false if not
	 */
	public static boolean isAuthenticated() {
		return SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				&& !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
	}

	/**
	 * Gets the username of the user in session
	 * @return {@link String} The username of the user in the current session
	 */
	public static String getUsername() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	/**
	 * Gets the user in session
	 * @return {@link User} The user in the current session
	 */
	public static User getUser() {
		return ((UserPrincipalImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
	}

	/**
	 * Gets the IP from the current request
	 * @return {@link String} The IP of the user in the current session
	 */
	public static String getIp() {
		return ((WebAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getRemoteAddress();
	}
	
	/**
	 * Checks if the user is using the Spanish locale
	 * @return true if the locale is Spanish, false otherwise
	 */
	public static boolean isSpanishLocale() {
		return Locale.forLanguageTag("es-ES").equals(LocaleContextHolder.getLocale());
	}
	
}
