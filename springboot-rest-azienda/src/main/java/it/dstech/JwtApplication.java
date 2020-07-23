package it.dstech;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import it.dstech.model.Authority;
import it.dstech.model.AuthorityName;
import it.dstech.model.User;
import it.dstech.repository.AuthorityRepository;
import it.dstech.repository.UserRepository;

@EnableJpaRepositories
@SpringBootApplication
public class JwtApplication {


//	@Autowired
//	private DataSource datasource;
//	@Autowired
//	private ApplicationContext webApplicationContext;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner loadData(UserRepository userRepository, AuthorityRepository authorityRepository) {
		return (args) -> {

			User user=userRepository.findByUsername("admin");

			if(user == null){

				/**
				 * Inizializzo i dati del mio test
				 */


				Authority authorityAdmin=new Authority();
				authorityAdmin.setName(AuthorityName.ROLE_ADMIN);
				authorityAdmin=authorityRepository.save(authorityAdmin);

				Authority authorityUser=new Authority();
				authorityUser.setName(AuthorityName.ROLE_USER);
				authorityUser=authorityRepository.save(authorityUser);


				List<Authority> authorities = Arrays.asList(new Authority[] {authorityAdmin,authorityUser});


				user = new User();
				user.setAuthorities(authorities);
				user.setEnabled(true);
				user.setUsername("admin");
				user.setPassword(passwordEncoder.encode("admin"));

				user = userRepository.save(user);

			}
		};
	}
	
	public static void main(String[] args) {
		SpringApplication.run(JwtApplication.class, args);
	}
}
