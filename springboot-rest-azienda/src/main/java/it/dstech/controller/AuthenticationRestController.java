package it.dstech.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import it.dstech.model.Authority;
import it.dstech.model.Servizio;
import it.dstech.model.User;
import it.dstech.security.JwtAuthenticationRequest;
import it.dstech.security.JwtTokenUtil;
import it.dstech.service.JwtAuthenticationResponse;
import it.dstech.service.ServizioService;
import it.dstech.service.UserService;

@RestController
public class AuthenticationRestController {

	@Value("${jwt.header}")
	private String tokenHeader;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private ServizioService servizioService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "public/login", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest,
			HttpServletResponse response) throws AuthenticationException, JsonProcessingException {

		// Effettuo l autenticazione
		final Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
						authenticationRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Genero Token
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails);

		response.setHeader(tokenHeader, token);
		// Ritorno il token
		return ResponseEntity
				.ok(new JwtAuthenticationResponse(userDetails.getUsername(), userDetails.getAuthorities()));
	}

	@RequestMapping(value = "protected/refresh-token", method = RequestMethod.GET)
	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request,
			HttpServletResponse response) {
		String token = request.getHeader(tokenHeader);
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (jwtTokenUtil.canTokenBeRefreshed(token)) {
			String refreshedToken = jwtTokenUtil.refreshToken(token);
			response.setHeader(tokenHeader, refreshedToken);

			return ResponseEntity
					.ok(new JwtAuthenticationResponse(userDetails.getUsername(), userDetails.getAuthorities()));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}

	@RequestMapping(value = "protected/creaServizio", method = RequestMethod.POST)
	public boolean creaServizio(@RequestBody Servizio servizio, HttpServletRequest request,
			HttpServletResponse response) {
		String token = request.getHeader(tokenHeader);
		System.out.println(token);
		Authority authority = new Authority();
		authority.getName();
		for (GrantedAuthority authority2 : jwtTokenUtil.getUserDetails(token).getAuthorities()) {
			if (authority2.getAuthority().equals("ROLE_ADMIN")) {
				servizio.setQuantitàDisponibile(servizio.getQuantita());
				servizioService.save(servizio);
				return true;
			}
		}
		return false;
	}
	
	@RequestMapping(value = "public/listaServizi", method = RequestMethod.GET)
	public List<Servizio> listaServizi() {
		return servizioService.listaServizi();
	}
	
	@RequestMapping(value = "protected/prenotaServizio", method = RequestMethod.POST)
	public boolean prenotaServizio(@RequestBody Servizio servizio, HttpServletRequest request,
			HttpServletResponse response) {
		String token = request.getHeader(tokenHeader);
		Authority authority = new Authority();
		authority.getName();
		User user = userService.findUserByUsername(jwtTokenUtil.getUserDetails(token).getUsername());
		Servizio service = servizioService.get(servizio.getId());
		if(userService.checkEsistenzaServizio(user, service) || user.getListaServizi().size() == 3 || service.getQuantitàDisponibile() == 0) {
			return false;
		}
		service.setQuantitàDisponibile(service.getQuantitàDisponibile()-1);
		service.getUser().add(user);
		servizioService.save(service);
		service.setQuantita(1);
		user.getListaServizi().add(service);
		userService.save(user);
		return true;
	}
	
	@RequestMapping(value = "protected/reset", method = RequestMethod.POST)
	public boolean reset(HttpServletRequest request,
			HttpServletResponse response) {
		String token = request.getHeader(tokenHeader);
		Authority authority = new Authority();
		authority.getName();
		for (GrantedAuthority authority2 : jwtTokenUtil.getUserDetails(token).getAuthorities()) {
			if (authority2.getAuthority().equals("ROLE_ADMIN")) {
				userService.reset();
				servizioService.reset();
				return true;
			}
		}
		return false;
	}

}