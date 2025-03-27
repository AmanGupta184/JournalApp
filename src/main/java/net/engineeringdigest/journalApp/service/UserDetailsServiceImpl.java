package net.engineeringdigest.journalApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import net.engineeringdigest.journalApp.entity.User;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.findByUserName(username);
		if (user != null) {
			UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
					.username(user.getUserName()).password(user.getPassword())
					.roles(user.getRoles().toArray(new String[0])).build();
			return userDetails;
		}
		throw new UsernameNotFoundException("User not found with username" + username);
	}

}
