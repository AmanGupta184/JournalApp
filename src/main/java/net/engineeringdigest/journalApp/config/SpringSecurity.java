package net.engineeringdigest.journalApp.config;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import net.engineeringdigest.journalApp.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SpringSecurity {

	@Bean
	UserDetailsServiceImpl userDetailsService() {
		return new UserDetailsServiceImpl();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(Authorize -> {
			try {
				Authorize.requestMatchers("/journal/**", "/users/**").authenticated().and().httpBasic();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).authorizeHttpRequests(Authorize -> {
			try {
				Authorize.requestMatchers("/admin/**").hasRole("ADMIN").anyRequest().permitAll().and().httpBasic();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		})

				.sessionManagement(Management -> Management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.csrf(csrf -> csrf.disable());
		return http.build();
	}

	@Bean
	AuthenticationManager authenticationManager(UserDetailsServiceImpl userDetailsService) {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return new ProviderManager(Collections.singletonList(authProvider));
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
