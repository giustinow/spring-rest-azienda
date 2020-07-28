package it.dstech.service;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import it.dstech.model.Authority;
import it.dstech.model.AuthorityName;
import it.dstech.model.Servizio;
import it.dstech.model.User;
import it.dstech.repository.AuthorityRepository;
import it.dstech.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	public void save(User user) {
		User nuovoUser = userRepository.findByUsername(user.getUsername());

		if (nuovoUser == null) {
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			user.setEnabled(true);

			Authority userRole = authorityRepository.findByName(AuthorityName.ROLE_USER);
			user.setAuthorities(Arrays.asList(userRole));

		}
		userRepository.save(user);
	}

	public User get(Long id) {
		return userRepository.findById(id).get();
	}

	public void delete(Long id) {
		userRepository.deleteById(id);
	}

	public List<User> findAll() {
		return (List<User>) userRepository.findAll();
	}

	public User findUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public boolean checkEsistenzaServizio(User user, Servizio service) {
		for (Servizio servizio : user.getListaServizi()) {
			if (servizio.getNome().equals(service.getNome())) {
				return true;
			}
		}
		return false;
	}
	
	public void reset() {
		for (User user : findAll()) {
			user.setListaServizi(null);
			save(user);
		}
	}

}
