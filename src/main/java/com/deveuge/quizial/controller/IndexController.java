package com.deveuge.quizial.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.deveuge.quizial.enums.Alert;
import com.deveuge.quizial.enums.QuizSearchType;
import com.deveuge.quizial.enums.VerificationType;
import com.deveuge.quizial.model.entity.User;
import com.deveuge.quizial.model.entity.VerificationToken;
import com.deveuge.quizial.model.service.CategoryService;
import com.deveuge.quizial.model.service.QuizService;
import com.deveuge.quizial.model.service.UserService;
import com.deveuge.quizial.model.service.VerificationTokenService;
import com.deveuge.quizial.util.Constants;
import com.deveuge.quizial.util.SecurityUtils;
import com.deveuge.quizial.util.ViewUtils;
import com.deveuge.quizial.util.aspect.MethodLogging;
import com.deveuge.quizial.util.pagination.PageRender;
import com.deveuge.quizial.view.dto.QuizDTO;
import com.deveuge.quizial.view.dto.UserDTO;
import com.deveuge.quizial.view.filter.QuizFilter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Controller
@SessionAttributes("filter")
public class IndexController {
	
	private static final String INDEX_TAB = "indexTab";
	private static final int INDEX_REGISTER_TAB = 1;
	private static final int INDEX_RESET_TAB = 3;
	private static final String REDIRECT_TO_INDEX = "redirect:/";
	private static final String MODEL_ATTRIBUTE_CATEGORIES = "categories";
	
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private UserService userService;
	@Autowired
	private QuizService quizService;
	@Autowired
	private VerificationTokenService verificationTokenService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@ModelAttribute("filter")
	public QuizFilter getQuizFilter() {
		return new QuizFilter();
	}
	
	/**
	 * Index controller
	 * @param model {@link Model} Container that holds the data of the application
	 * @return {@link String} The dashboard page if the user is authenticated or the login page otherwise
	 */
	@MethodLogging
	@GetMapping("/")
	public String index(Model model) {
		if(SecurityUtils.isAuthenticated()) {
			// Dashboard page
			return search(model, null, Integer.valueOf(Constants.RESULTS_DASHBOARD));
		} else {
			// Index page
			if (!model.containsAttribute("user")) {
				model.addAttribute("user", new UserDTO());
			}
			return "index";
		}
	}

	/**
	 * Register controller. Creates a new user and sends a verification email
	 * @param user {@link UserDTO} Data of the user to create entered by the user making the request
	 * @param result {@link BindingResult} Results that holds possible validation errors
	 * @param redir {@link RedirectAttributes} Redirect and flash attributes 
	 * @param locale {@link Locale} Specific geographical, political, or cultural region
	 * @return {@link String} Redirects to the login page
	 */
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("user") UserDTO user, BindingResult result, RedirectAttributes redir, Locale locale) {
		if (result.hasErrors()) {
			redir.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
			redir.addFlashAttribute("user", user);
	    } else {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			User userEntity = new User(user);
			userService.save(userEntity);
			boolean success = verificationTokenService.sendEmail(userEntity, VerificationType.CONFIRM, locale);
			if(success) {
				ViewUtils.createAlert(redir, Alert.SUCCESS, "user.success.create");
			} else {
				ViewUtils.createAlert(redir, Alert.DANGER, "user.error.create");
			}
	    }
		redir.addFlashAttribute(INDEX_TAB, INDEX_REGISTER_TAB);
		return REDIRECT_TO_INDEX;
	}
	
	/**
	 * Confirm new account controller. Enables a new user.
	 * @param token {@link String} New account verification token 
	 * @param redir {@link RedirectAttributes} Redirect and flash attributes
	 * @return {@link String} Redirects to the login page alerting the user if the activation was successful
	 */
	@GetMapping("/confirm")
	public String confirmUser(@RequestParam String token, RedirectAttributes redir) {
		VerificationToken verificationToken = verificationTokenService.findOne(token);
		if (verificationToken == null || !verificationToken.isValidConfirmToken()) {
			ViewUtils.createAlert(redir, Alert.DANGER, "user.error.activate");
			redir.addFlashAttribute(INDEX_TAB, INDEX_REGISTER_TAB);
	    } else {
			verificationToken.getUser().setEnabled(true);
			userService.save(verificationToken.getUser());
			verificationTokenService.delete(token);
			ViewUtils.createAlert(redir, Alert.SUCCESS, "user.success.activate");
	    }
		return REDIRECT_TO_INDEX;
	}

	/**
	 * Process the user reset request. Creates a new token and sends an email with the instructions. 
	 * @param user {@link UserDTO} Data of the user to reset entered by the user making the request
	 * @param redir {@link RedirectAttributes} Redirect and flash attributes
	 * @param locale {@link Locale} Specific geographical, political, or cultural region
	 * @return {@link String} Redirects to the login page alerting the user if the reset request was successful
	 */
	@PostMapping("/reset")
	public String resetUser(@ModelAttribute UserDTO user, RedirectAttributes redir, Locale locale, HttpServletRequest request) {
		User userEntity = userService.findByEmail(user.getResetEmail());
		if(userEntity == null) {
			redir.addFlashAttribute("user", user);
			ViewUtils.createAlert(redir, Alert.DANGER, "user.error.reset.email");
			redir.addFlashAttribute(INDEX_TAB, INDEX_RESET_TAB);
		} else {
			boolean success = verificationTokenService.sendEmail(userEntity, VerificationType.RESET, locale);
			if(success) {
				ViewUtils.createAlert(redir, Alert.PRIMARY, "user.info.reset");
			} else {
				ViewUtils.createAlert(redir, Alert.DANGER, "user.error.reset");
			}
		}
		return REDIRECT_TO_INDEX;
	}

	/**
	 * Resets the user account. Set the new password provided in the reset email.
	 * @param token {@link String} Token used to reset the user account 
	 * @param redir {@link RedirectAttributes} Redirect and flash attributes
	 * @return {@link String} Redirects to the login page alerting the user if the reset of the account was successful
	 */
	@GetMapping("/reset")
	public String confirmReset(@RequestParam String token, RedirectAttributes redir) {
		VerificationToken verificationToken = verificationTokenService.findOne(token);
		if (verificationToken == null || !verificationToken.isValidResetToken()) {
			ViewUtils.createAlert(redir, Alert.DANGER, "user.error.reset");
			redir.addFlashAttribute(INDEX_TAB, INDEX_RESET_TAB);
	    } else {
			verificationToken.getUser().setPassword(passwordEncoder.encode(verificationToken.getUUIDTokenPart()));
			userService.save(verificationToken.getUser());
			verificationTokenService.delete(token);
			ViewUtils.createAlert(redir, Alert.SUCCESS, "user.success.reset");
	    }
		return REDIRECT_TO_INDEX;
	}
	
	/**
	 * Performs a new search of quizzes. Called when the user makes a new search through the filters.
	 * @param model {@link Model} Container that holds the data of the application
	 * @param filter {@link QuizFilter} Filter with the current search
	 * @param size {@link Integer} Number of results per page
	 * @return {@link String} Reloads the dashboard showing the first page results
	 */
	@PostMapping("/search")
	public String search(Model model, @ModelAttribute("filter") QuizFilter filter, @RequestParam(defaultValue = Constants.RESULTS_DASHBOARD) int size) {
		List<QuizAttribute> allQuizzes = new ArrayList<>();
		for(QuizSearchType searchType : QuizSearchType.values()) {
			Page<QuizDTO> quizzes = findQuizzesBySeachType(searchType, filter, PageRequest.of(Constants.RESULTS_FIRST_PAGE, size));
			if(quizzes != null) {
				allQuizzes.add(new QuizAttribute(searchType, quizzes));
			}
		}
		model.addAttribute(MODEL_ATTRIBUTE_CATEGORIES, categoryService.findAll());
		model.addAttribute("allQuizzes", allQuizzes);
		return "dashboard";
	}
	
	/**
	 * Shows a specific page of the current search results.
	 * @param model {@link Model} Container that holds the data of the application
	 * @param filter {@link QuizFilter} Filter with the current search. Recovered from the session attributes
	 * @param type {@link String} QuizSearchType name of the selected tab (popular, new, friends...)
	 * @param page {@link Integer} Results page to return
	 * @param size {@link Integer} Number of results per page
	 * @return {@link String} Returns the quizzes container fragment showing the requested page results
	 */
	@GetMapping("/search")
	public String list(Model model, @ModelAttribute("filter") QuizFilter filter, @RequestParam(required = false) String type,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = Constants.RESULTS_DASHBOARD) int size) {
		if(type == null) {
			return search(model, filter, Integer.valueOf(Constants.RESULTS_DASHBOARD));
		}
		QuizSearchType searchType = QuizSearchType.valueOf(type);
		Page<QuizDTO> quizzes = findQuizzesBySeachType(searchType, filter, PageRequest.of(page, size));
		List<QuizAttribute> allQuizzes = Arrays.asList(new QuizAttribute(searchType, quizzes));
		model.addAttribute("allQuizzes", allQuizzes);
		return "dashboard :: quizzes-container";
	}
	
	private Page<QuizDTO> findQuizzesBySeachType(QuizSearchType searchType, QuizFilter filter, PageRequest pageRequest) {
		switch(searchType) {
		case USER:
			return (filter == null) ? null : quizService.find(filter, pageRequest);
		case POPULAR:
			return quizService.find(new QuizFilter(), pageRequest); // TODO
		case NEW:
			return quizService.find(new QuizFilter(), pageRequest); // TODO
		case FRIENDS:
			return quizService.find(new QuizFilter(), pageRequest); // TODO
		}
		return null;
	}
	
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	@Setter
	private class QuizAttribute {
		String name;
		Page<QuizDTO> quizzes;
		PageRender<QuizDTO> page;
		QuizSearchType searchType;
		
		public QuizAttribute(QuizSearchType searchType, Page<QuizDTO> quizzes) {
			this.name = searchType.getTabName();
			this.quizzes = quizzes;
			this.page = new PageRender<>("/search?type=" + searchType.name(), quizzes);
			this.searchType = searchType;
		}
	}

}