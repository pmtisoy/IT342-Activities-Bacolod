package com.bacolod.google.oauth.controller;

import com.bacolod.google.oauth.service.peopleService;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import com.google.api.services.people.v1.model.Person;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import java.util.stream.Collectors;

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
        List<String> contacts = peopleService.getContacts(authentication);
        ModelAndView modelAndView = new ModelAndView("contacts"); // Name of your template
        modelAndView.addObject("contacts", contacts); // Add data to the model
        return modelAndView;
    }
}
