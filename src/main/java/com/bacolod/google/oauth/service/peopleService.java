package com.bacolod.google.oauth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class peopleService {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    public List<Person> getContacts(OAuth2AuthenticationToken authentication) throws IOException, GeneralSecurityException {
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(), authentication.getName());

        if (authorizedClient == null) {
            throw new IllegalStateException("Authorized client not found.");
        }

        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken.getTokenValue());

        PeopleService peopleService = new PeopleService.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("Oauth") // Replace with your app name
                .build();

        ListConnectionsResponse response = peopleService.people().connections()
                .list("people/me")
                .setPersonFields("names,emailAddresses,phoneNumbers") // Include phone numbers
                .execute();

        List<Person> connections = response.getConnections();

        if (connections == null || connections.isEmpty()) {
            return List.of(); // Return an empty list
        }
        else{

        return connections; // Return the list of Person objects
        }
    }
}