package org.sysc.ama.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.sysc.ama.services.CustomUserDetails;

@Controller
public class StaticController {
	
	@RequestMapping("/")
	public String home(@AuthenticationPrincipal CustomUserDetails principal, Model model){
		model.addAttribute("userId", principal.getUser().getId());
		return "index";
	}

	@RequestMapping("/login")
	public String login(){
		return "login";
	}
	
	@RequestMapping("/profile")
	public String profile(@AuthenticationPrincipal CustomUserDetails principal, Model model) {
		model.addAttribute("userId", principal.getUser().getId());
		return "profile";
	}

}
