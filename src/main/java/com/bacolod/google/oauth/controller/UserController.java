package com.bacolod.google.oauth.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bacolod.google.oauth.service.peopleService;
import com.google.api.services.people.v1.model.Person;

@Controller
public class UserController {

    @Autowired
    private peopleService peopleService;

    @GetMapping
    public String index() {
        return "<h1> Welcome, This is the landing page.</h1>";
    }

    @GetMapping({"/user-info"})
    public ModelAndView getUserInfo(@AuthenticationPrincipal OAuth2User oAuth2User) {
        Map<String, Object> userInfo = oAuth2User != null ? oAuth2User.getAttributes() : Collections.emptyMap();
        ModelAndView modelAndView = new ModelAndView("user-info"); // Name of your template
        modelAndView.addObject("userInfo", userInfo); // Add user info to the model
        return modelAndView;
    }
    @GetMapping("/contacts")
    public ModelAndView getContacts(OAuth2AuthenticationToken authentication) throws IOException, GeneralSecurityException {
        List<Person> contacts = peopleService.getContacts(authentication);
        ModelAndView modelAndView = new ModelAndView("contacts"); // Name of your template
        modelAndView.addObject("contacts", contacts); // Add data to the model
        return modelAndView;
    }

    
}
