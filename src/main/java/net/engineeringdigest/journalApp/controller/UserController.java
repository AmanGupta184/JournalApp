package net.engineeringdigest.journalApp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;

	@PutMapping
	public ResponseEntity<?> updateUser(@RequestBody User user) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		User userInDb = userService.findByUserName(userName);
		userInDb.setUserName(user.getUserName());
		userInDb.setPassword(user.getPassword());
		userService.saveEntry(userInDb);
		return new ResponseEntity<>(userInDb, HttpStatus.NO_CONTENT);
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteUserById(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		userRepository.deleteByUserName(authentication.getName());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
