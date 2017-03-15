package org.sysc.ama.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StaticController {
	
	@RequestMapping("/")
	public String home(){
		return "index";
	}

	@RequestMapping("/login")
	public String login(){
		return "login";
	}

}
