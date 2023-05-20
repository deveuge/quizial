package com.deveuge.quizial.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.deveuge.quizial.model.service.QuizService;
import com.deveuge.quizial.model.service.UserService;
import com.deveuge.quizial.util.Constants;
import com.deveuge.quizial.util.SecurityUtils;
import com.deveuge.quizial.util.pagination.PageRender;
import com.deveuge.quizial.view.dto.QuizDTO;
import com.deveuge.quizial.view.dto.UserDTO;

@Controller
@RequestMapping("/u")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private QuizService quizService;

	/**
	 * Displays the user profile page.
	 * @param model {@link Model} Container that holds the data of the application
	 * @param id {@link String} Identifier of the user to display
	 * @return {@link String} The user profile page in view mode
	 */
	@GetMapping("/{id}")
	public String get(Model model, @PathVariable String id) {
		model.addAttribute("user", userService.get(id));
		model.addAttribute("isFriend", userService.isUserFriend(SecurityUtils.getUsername(), id));
		setQuizzes(model, id, Constants.RESULTS_FIRST_PAGE);
		setFavorites(model, id, Constants.RESULTS_FIRST_PAGE);
		setFriends(model, id, Constants.RESULTS_FIRST_PAGE);
		return "user/view";
	}
	
	/**
	 * Shows a specific page of the quizzes created by a user.
	 * @param model {@link Model} Container that holds the data of the application
	 * @param id {@link String} Identifier of the user
	 * @param page {@link Integer} Results page to return
	 * @return {@link String} Returns the quizzes container fragment showing the requested page results
	 */
	@GetMapping("/{id}/q")
	public String getQuizzes(Model model, @PathVariable String id, @RequestParam(defaultValue = "0") int page) {
		setQuizzes(model, id, page);
		return "fragments/templates/user-view :: quizzes";
	}
	
	/**
	 * Shows a specific page of the quizzes marked as favorite by a user.
	 * @param model {@link Model} Container that holds the data of the application
	 * @param id {@link String} Identifier of the user
	 * @param page {@link Integer} Results page to return
	 * @return {@link String} Returns the favorite quizzes container fragment showing the requested page results
	 */
	@GetMapping("/{id}/fav")
	public String getFavorites(Model model, @PathVariable String id, @RequestParam(defaultValue = "0") int page) {
		setFavorites(model, id, page);
		return "fragments/templates/user-view :: favorites";
	}
	
	/**
	 * Shows a specific page of a user's friends.
	 * @param model {@link Model} Container that holds the data of the application
	 * @param id {@link String} Identifier of the user
	 * @param page {@link Integer} Results page to return
	 * @return {@link String} Returns the favorite quizzes container fragment showing the requested page results
	 */
	@GetMapping("/{id}/f")
	public String getFriends(Model model, @PathVariable String id, @RequestParam(defaultValue = "0") int page) {
		setFriends(model, id, page);
		return "fragments/templates/user-view :: friends";
	}
	
	/**
	 * Adds a friend to the current user in the session.
	 * @param username {@link String} Username of the user to add as a friend
	 * @return
	 */
	@PutMapping("/{username}/friend")
	public ResponseEntity<String> addFriend(@PathVariable String username) {
		userService.addFriend(SecurityUtils.getUsername(), username);
        return new ResponseEntity<>(username, HttpStatus.OK);
    }
	
	/**
	 * Deletes a friend to the current user in the session.
	 * @param username {@link String} Username of the user to remove as a friend
	 * @return
	 */
	@DeleteMapping("/{username}/friend")
	public ResponseEntity<String> removeFriend(@PathVariable String username) {
		userService.removeFriend(SecurityUtils.getUsername(), username);
        return new ResponseEntity<>(username, HttpStatus.OK);
    }
	
	/**
	 * Adds or removes a friend to the current user in the session.
	 * @param model {@link Model} Container that holds the data of the application
	 * @param username {@link String} Username of the user to add or remove as a friend
	 * @param method {@link String} HTTP Method to use: PUT to add a friend, DELETE to remove a friend
	 * @return Redirects to the user profile page
	 */
	@PostMapping("/{username}/friend")
	public String addOrRemoveFriend(Model model, @PathVariable String username, @RequestParam(name = "_method") String method) {
		RequestMethod requestMethod = RequestMethod.valueOf(method);
		switch(requestMethod) {
		case PUT:
			addFriend(username);
			break;
		case DELETE:
			removeFriend(username);
			break;
		default:
			break;
		}
        return String.format("redirect:/u/%s", username);
    }
	
	/**
	 * Sets the user quizzes on the model.
	 * @param model {@link Model} Container that holds the data of the application
	 * @param username {@link String} Identifier of the user
	 * @param page {@link Integer} Results page to return
	 */
	private void setQuizzes(Model model, String username, int page) {
		setCurrentUser(model, username);
		Page<QuizDTO> quizzes = quizService.findByUsername(username, PageRequest.of(page, Constants.RESULTS_PER_PAGE));
		model.addAttribute("quizzes", quizzes);
		model.addAttribute("pageQuiz", new PageRender<>("/u/" + username + "/q", quizzes));
	}
	
	/**
	 * Sets the user favorite quizzes on the model.
	 * @param model {@link Model} Container that holds the data of the application
	 * @param username {@link String} Identifier of the user
	 * @param page {@link Integer} Results page to return
	 */
	private void setFavorites(Model model, String username, int page) {
		setCurrentUser(model, username);
		Page<QuizDTO> favorites = quizService.findFavorites(username, PageRequest.of(page, Constants.RESULTS_PER_PAGE));
		model.addAttribute("favorites", favorites);
		model.addAttribute("pageFav", new PageRender<>("/u/" + username + "/fav", favorites));
	}
	
	/**
	 * Sets the user friends on the model.
	 * @param model {@link Model} Container that holds the data of the application
	 * @param username {@link String} Identifier of the user
	 * @param page {@link Integer} Results page to return
	 */
	private void setFriends(Model model, String username, int page) {
		setCurrentUser(model, username);
		Page<UserDTO> friends = userService.findFriends(username, PageRequest.of(page, Constants.RESULTS_PER_PAGE));
		model.addAttribute("friends", friends);
		model.addAttribute("pageFriend", new PageRender<>("/u/" + username + "/f", friends));
	}
	
	/**
	 * Sets if the user on the model is the current user.
	 * @param model {@link Model} Container that holds the data of the application
	 * @param username {@link String} Identifier of the user
	 */
	private void setCurrentUser(Model model, String username) {
		model.addAttribute("isCurrentUser", SecurityUtils.getUsername().equalsIgnoreCase(username));
	}
}
