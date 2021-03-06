package com.mpp.gaskeun.controller;

import com.mpp.gaskeun.model.Customer;
import com.mpp.gaskeun.model.RentalProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@Slf4j
@RequestMapping("/")
public class BaseController {

    @GetMapping("")
    public String displayHomePage(@AuthenticationPrincipal UserDetails user, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isNotLoggedIn = authentication == null || authentication instanceof AnonymousAuthenticationToken;
        if (!isNotLoggedIn) {
            if (user instanceof RentalProvider) {
                model.addAttribute("userType", "provider");
            } else if (user instanceof Customer) {
                model.addAttribute("userType", "customer");
            }
        }
        model.addAttribute("notLoggedIn", isNotLoggedIn);

        return "index";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }

        return "redirect:/";
    }


}
