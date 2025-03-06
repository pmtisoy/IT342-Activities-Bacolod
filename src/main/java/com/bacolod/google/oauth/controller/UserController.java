package com.bacolod.google.oauth.controller;

import com.bacolod.google.oauth.service.peopleService;
import com.google.api.services.people.v1.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/addContact")
    public String addContactForm() {
        return "addContact";
    }

    @PostMapping("/addContact")
    public String addContact(OAuth2AuthenticationToken authentication, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String phoneNumber, Model model) throws IOException, GeneralSecurityException {
        peopleService.createContact(authentication, firstName, lastName, email, phoneNumber);
        model.addAttribute("message", "Contact added successfully!");
        return "addContact";
    }

    @GetMapping("/editContact/{resourceName}")
    public String editContactForm(OAuth2AuthenticationToken authentication, @PathVariable String resourceName, Model model) throws IOException, GeneralSecurityException {
        Person person = peopleService.getPeopleService(peopleService.getAuthorizedClient(authentication)).people().get(resourceName).setPersonFields("names,emailAddresses,phoneNumbers").execute();
        model.addAttribute("contact", person);
        return "editContact";
    }

    @PostMapping("/editContact/{resourceName}")
    public String editContact(OAuth2AuthenticationToken authentication, @PathVariable String resourceName, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String phoneNumber, Model model) throws IOException, GeneralSecurityException {
        peopleService.updateContact(authentication, resourceName, firstName, lastName, email, phoneNumber);
        model.addAttribute("message", "Contact updated successfully!");
        return "redirect:/contacts";
    }

    @GetMapping("/deleteContact/{resourceName}")
    public String deleteContact(OAuth2AuthenticationToken authentication, @PathVariable String resourceName) throws IOException, GeneralSecurityException {
        peopleService.deleteContact(authentication, resourceName);
        return "redirect:/contacts";
    }

}
