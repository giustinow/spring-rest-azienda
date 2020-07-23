package it.dstech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.dstech.model.User;
import it.dstech.service.UserService;

@RestController
public class WebController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "public/registrazione", method = RequestMethod.POST)
	public @ResponseBody boolean registrazione(@RequestBody User user) {
		userService.save(user);
		return true;
	}

}
