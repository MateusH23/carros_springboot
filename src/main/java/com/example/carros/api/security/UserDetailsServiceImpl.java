package com.example.carros.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.carros.domain.User;
import com.example.carros.domain.UserRepository;

@Service(value = "userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByLogin(username);
		
		if(user == null) {
			throw new UsernameNotFoundException("User not found!");
		}
		
		return user;
		
		//return User.withUsername(username).password(user.getSenha()).roles("USER").build();
		
		/*BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		if(username.equals("user")) {
			return User.withUsername(username).password(encoder.encode("user")).roles("USER").build();
		} else if(username.equals("admin")) {
			return User.withUsername(username).password(encoder.encode("admin")).roles("USER", "ADMIN").build();
		}
		
		throw new UsernameNotFoundException("User not found!");*/
	}

}
