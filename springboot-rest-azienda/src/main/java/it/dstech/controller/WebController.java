package it.dstech.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {
	@RequestMapping(value = "public/hello", method = RequestMethod.GET)
	public @ResponseBody String publicHelloWorld() {
		return "Ciao Mondo-Pubblico";
	}
}
