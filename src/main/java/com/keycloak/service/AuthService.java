package com.keycloak.service;

import java.util.HashMap;
import java.util.Map;

import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.keycloak.dto.LoginRequest;
import com.keycloak.dto.LoginResponse;

@Service
public class AuthService {

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    public LoginResponse login(LoginRequest request) {

        Map<String, Object> credentials = new HashMap<>();

        credentials.put("secret", clientSecret);
        credentials.put("grant_type", "password");

        Configuration config =
                new Configuration(
                        authServerUrl,
                        realm,
                        clientId,
                        credentials,
                        null);

        AuthzClient authzClient =
                AuthzClient.create(config);

        AccessTokenResponse token =
                authzClient.obtainAccessToken(
                        request.getUserName(),
                        request.getPassword());

        LoginResponse response = new LoginResponse();

        response.setJwtToken(token.getToken());
        response.setUsername(request.getUserName());

        response.setOrgCode("AVISYS");
        response.setDivision("ADMIN");
        response.setRole("ADMIN");

        return response;
    }
}