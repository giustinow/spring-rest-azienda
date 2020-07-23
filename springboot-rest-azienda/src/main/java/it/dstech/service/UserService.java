package it.dstech.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.dstech.model.User;
import it.dstech.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public void save(User user) {
		userRepository.save(user);
	}
	
	public User get(Long id) {
		return userRepository.findById(id).get();
	}

	public void delete(Long id) {
		userRepository.deleteById(id);
	}
	
	public List<User> listaStudenti() {
		return (List<User>) userRepository.findAll();
	}

	public User findUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	
	
}
