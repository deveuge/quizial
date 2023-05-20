package com.deveuge.quizial.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.deveuge.quizial.enums.Alert;
import com.deveuge.quizial.model.service.AnswerService;
import com.deveuge.quizial.model.service.CategoryService;
import com.deveuge.quizial.model.service.QuizService;
import com.deveuge.quizial.model.service.ResultService;
import com.deveuge.quizial.model.service.UserService;
import com.deveuge.quizial.util.SecurityUtils;
import com.deveuge.quizial.util.ViewUtils;
import com.deveuge.quizial.view.dto.QuizDTO;
import com.deveuge.quizial.view.dto.QuizFormDTO;

@Controller
@RequestMapping("/q")
public class QuizController {
	
	private static final String REDIRECT_QUIZ_URL = "redirect:/q/";
	private static final String MODEL_ATTRIBUTE_QUIZ = "quiz";
	private static final String MODEL_ATTRIBUTE_QUIZ_FORM = "quizForm";
	private static final String MODEL_ATTRIBUTE_RESULT = "result";
	private static final String MODEL_ATTRIBUTE_CATEGORIES = "categories";;
	private static final String MODEL_ATTRIBUTE_FAVORITE = "isFavorite";

	@Autowired
	private UserService userService;
	@Autowired
	private QuizService quizService;
	@Autowired
	private AnswerService answerService;
	@Autowired
	private ResultService resultService;
	@Autowired
	private CategoryService categoryService;
	
	/**
	 * Displays the quiz page ready to be taken.
	 * @param model {@link Model} Container that holds the data of the application
	 * @param id {@link Long} Identifier of the quiz that can be taken
	 * @return {@link String} The new quiz detail page in view mode
	 */
	@GetMapping("/{id}")
	public String get(Model model, @PathVariable Long id) {
		QuizDTO q = quizService.get(id);
		model.addAttribute(MODEL_ATTRIBUTE_QUIZ, q);
		model.addAttribute(MODEL_ATTRIBUTE_QUIZ_FORM, new QuizFormDTO(q.getQuestions().size(), quizService.findNext(id)));
		model.addAttribute(MODEL_ATTRIBUTE_FAVORITE, quizService.isFavorite(id, SecurityUtils.getUsername()));
		return "quiz/view";
	}
	
	/**
	 * Shows the quiz result according to the user's answers.
	 * @param model {@link Model} Container that holds the data of the application
	 * @param id {@link Long} Identifier of the quiz that has been taken
	 * @param quizForm {@link QuizFormDTO} Object containing the form information
	 * @return {@link String} Redirects to the quiz results page
	 */
	@PostMapping("/{id}")
	public String post(Model model, @PathVariable Long id, QuizFormDTO quizForm) {
		model.addAttribute(MODEL_ATTRIBUTE_QUIZ, quizService.get(id));
		model.addAttribute(MODEL_ATTRIBUTE_RESULT, resultService.get(id, calculateQuizResult(quizForm)));
		model.addAttribute(MODEL_ATTRIBUTE_QUIZ_FORM, quizForm);
		model.addAttribute(MODEL_ATTRIBUTE_FAVORITE, quizService.isFavorite(id, SecurityUtils.getUsername()));
		return "quiz/result";
	}
	
	/**
	 * Shows the form to create a new quiz.
	 * @param model {@link Model} Container that holds the data of the application
	 * @return {@link String} The new quiz form view
	 */
	@GetMapping("/new")
	public String create(Model model) {
		if (!model.containsAttribute(MODEL_ATTRIBUTE_QUIZ)) {
			model.addAttribute(MODEL_ATTRIBUTE_QUIZ, new QuizDTO());
		}
		model.addAttribute(MODEL_ATTRIBUTE_CATEGORIES, categoryService.findAll());
		return "quiz/new";
	}
	
	/**
	 * Creates a new quiz.
	 * @param quiz {@link QuizDTO} Object containing the quiz form information
	 * @param result {@link BindingResult} Results that holds possible validation errors
	 * @param redir {@link RedirectAttributes} Redirect and flash attributes
	 * @return {@link String} Redirects to the new quiz detail page
	 */
	@PostMapping("/new")
	public String create(@Valid @ModelAttribute("quiz") QuizDTO quiz, BindingResult result, RedirectAttributes redir) {
		if (result.hasErrors()) {
			quiz.setChildIds();
			redir.addFlashAttribute("org.springframework.validation.BindingResult.quiz", result);
			redir.addFlashAttribute(MODEL_ATTRIBUTE_QUIZ, quiz);
			return REDIRECT_QUIZ_URL + "new";
	    }
		quiz.cleanChildIds();
		Long savedQuizId = quizService.create(quiz);
		
		ViewUtils.createAlert(redir, Alert.SUCCESS, "quiz.success.create");
		return REDIRECT_QUIZ_URL + savedQuizId;
	}
	
	/**
	 * Shows the form to edit a specific quiz.
	 * @param id {@link Long} Identifier of the quiz to be edited
	 * @param model {@link Model} Container that holds the data of the application
	 * @param redir {@link RedirectAttributes} Redirect and flash attributes
	 * @return {@link String} The quiz detail page in edition mode
	 */
	@PreAuthorize("hasPermission(#id, 'QUIZ', 'ROLE_USER')")
	@GetMapping("/{id}/edit")
	public String edit(@Valid @PathVariable Long id, Model model, RedirectAttributes redir) {
		if (!model.containsAttribute(MODEL_ATTRIBUTE_QUIZ)) {
			model.addAttribute(MODEL_ATTRIBUTE_QUIZ, quizService.get(id));
		}
		model.addAttribute("isEdit", true);
		model.addAttribute(MODEL_ATTRIBUTE_CATEGORIES, categoryService.findAll());
		return "quiz/new";
	}
	
	/**
	 * Saves the modifications made to a specific quiz.
	 * @param quiz {@link QuizDTO} Object containing the quiz form information
	 * @param result {@link BindingResult} Results that holds possible validation errors
	 * @param redir {@link RedirectAttributes} Redirect and flash attributes
	 * @param model {@link Model} Container that holds the data of the application
	 * @return {@link String} Redirects to quiz detail page
	 */
	@PostMapping("/edit")
	public String edit(@Valid @ModelAttribute("quiz") QuizDTO quiz, BindingResult result, RedirectAttributes redir, Model model) {
		if (result.hasErrors()) {
			redir.addFlashAttribute("org.springframework.validation.BindingResult.quiz", result);
			redir.addFlashAttribute(MODEL_ATTRIBUTE_QUIZ, quiz);
			return REDIRECT_QUIZ_URL + quiz.getId() + "/edit";
	    }
		
		quizService.update(quiz);
		ViewUtils.createAlert(redir, Alert.SUCCESS, "quiz.success.edit");
		return REDIRECT_QUIZ_URL + quiz.getId();
	}
	
	/**
	 * Removes a specific quiz and redirects the user to the dashboard.
	 * @param model {@link Model} Container that holds the data of the application
	 * @param id {@link Long} Identifier of the quiz to be deleted
	 * @return {@link String} Redirects to the dashboard
	 */
	@PreAuthorize("hasPermission(#id, 'QUIZ', 'ROLE_USER')")
	@PostMapping("/{id}/delete")
	public String delete(Model model, @PathVariable Long id, RedirectAttributes redir) {
		quizService.delete(id);
		ViewUtils.createAlert(redir, Alert.SUCCESS, "quiz.success.delete");
		return "redirect:/";
	}

	/**
	 * Adds a quiz to the favorites of the current user in the session
	 * @param quizId {@link Long} ID of the quiz to add as a favorite
	 * @return
	 */
	@PutMapping("/{quizId}/fav")
	public ResponseEntity<Long> addFavorite(@PathVariable Long quizId) {
		userService.addFavorite(SecurityUtils.getUsername(), quizId);
        return new ResponseEntity<>(quizId, HttpStatus.OK);
    }
	
	/**
	 * Removes a quiz from the favorites of the current user in the session
	 * @param quizId {@link Long} ID of the quiz to add as a favorite
	 * @return
	 */
	@DeleteMapping("/{quizId}/fav")
	public ResponseEntity<Long> removeFavorite(@PathVariable Long quizId) {
		userService.removeFavorite(SecurityUtils.getUsername(), quizId);
        return new ResponseEntity<>(quizId, HttpStatus.OK);
    }
	
	/**
	 * Adds or removes a quiz favorite to the current user in the session.
	 * @param model {@link Model} Container that holds the data of the application
	 * @param username {@link String} ID of the quiz to add or remove as a favorite
	 * @param method {@link String} HTTP Method to use: PUT to add a favorite, DELETE to remove a favorite
	 * @return Returns the fragment of the favorite button
	 */
	@PostMapping("/{quizId}/fav")
	public String addOrRemoveFavorite(Model model, @PathVariable Long quizId, @RequestParam(name = "_method") String method) {
		RequestMethod requestMethod = RequestMethod.valueOf(method);
		switch(requestMethod) {
		case PUT:
			addFavorite(quizId);
			break;
		case DELETE:
			removeFavorite(quizId);
			break;
		default:
			break;
		}
		model.addAttribute("quizId", quizId);
		model.addAttribute(MODEL_ATTRIBUTE_FAVORITE, quizService.isFavorite(quizId, SecurityUtils.getUsername()));
		return "quiz/view :: fav-button";
    }
	
	/**
	 * Calculates the quiz result according to the user's answers.
	 * @param quizForm {@link QuizFormDTO} Object containing the form information
	 * @return {@link String} The letter corresponding to the result of the quiz
	 */
	private String calculateQuizResult(QuizFormDTO quizForm) {
		List<String> results = new ArrayList<>();
		Arrays.stream(quizForm.getAnswersId()).forEach(answerId -> results.add(answerService.findOne(answerId).getResultLetter()));
		
		return results.stream()
	      .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
	      .entrySet()
	      .stream()
	      .max(Map.Entry.comparingByValue())
	      .map(Map.Entry::getKey).orElse(null);
	}
}
