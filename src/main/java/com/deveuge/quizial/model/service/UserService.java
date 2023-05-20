package com.deveuge.quizial.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.deveuge.quizial.model.dao.UserDAO;
import com.deveuge.quizial.model.entity.Quiz;
import com.deveuge.quizial.model.entity.User;
import com.deveuge.quizial.view.dto.UserDTO;
import com.deveuge.quizial.view.mapper.UserMapper;
 
@Service
public class UserService {

	@Autowired
	private UserDAO userDAO;
	@Autowired
	private QuizService quizService;

	@Transactional(readOnly=true)
	public UserDTO get(String id) {
		return userDAO.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}
	
	@Transactional(readOnly = true)
	public Page<UserDTO> findFriends(String username, Pageable pageRequest) {
        return mapToResults(userDAO.findByFriendUsersFriendUsername(username, pageRequest));
	}
	
	@Transactional(readOnly=true)
	public User findByUsername(String id) {
		return userDAO.findById(id).orElse(null);
	}
	
	@Transactional(readOnly=true)
	public User findByEmail(String email) {
		return userDAO.findByEmail(email);
	}
	
	@Transactional(readOnly = true)
	public User findByUsernameOrEmail(String username, String email) {
		return userDAO.findByUsernameOrEmail(username, email);
	}
	
	@Transactional(readOnly=true)
	public int countByIp(String ip) {
		return userDAO.countByIp(ip);
	}
	
	@Transactional
	public void save(User user) {
		userDAO.save(user);
	}

	@Transactional
	public void delete(String id) {
		userDAO.deleteById(id);
	}
	
	@Transactional(readOnly=true)
	public boolean isUserFriend(String username, String friend) {
		return userDAO.findByUsernameAndFriendUsersUsername(username, friend).isPresent();
	}
	
	@Transactional
	public void addFriend(String currentUsername, String friendName) {
		User friend = findByUsername(friendName);
		if(friend == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		User currentUser = findByUsername(currentUsername);
		currentUser.getFriendUsers().add(friend);
		save(currentUser);
	}
	
	@Transactional
	public void removeFriend(String currentUsername, String friendName) {
		User friend = findByUsername(friendName);
		if(friend == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		User currentUser = findByUsername(currentUsername);
		currentUser.getFriendUsers().remove(friend);
		save(currentUser);
	}
	
	@Transactional
	public void addFavorite(String currentUsername, Long quizId) {
		Quiz quiz = quizService.findById(quizId);
		if(quiz == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		User currentUser = findByUsername(currentUsername);
		currentUser.getFavoriteQuizzes().add(quiz);
		save(currentUser);
	}
	
	@Transactional
	public void removeFavorite(String currentUsername, Long quizId) {
		Quiz quiz = quizService.findById(quizId);
		if(quiz == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		User currentUser = findByUsername(currentUsername);
		currentUser.getFavoriteQuizzes().remove(quiz);
		save(currentUser);
	}
	
	private UserDTO mapToDTO(final User user) {
    	return UserMapper.INSTANCE.userToUserDTO(user);
    }
	
	private Page<UserDTO> mapToResults(Page<User> results) {
    	return results.map(this::mapToDTO);
    }
	
}
