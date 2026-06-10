package com.keycloak.service;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
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

        try {

            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(authServerUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.PASSWORD)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .username(request.getUserName())
                    .password(request.getPassword())
                    .build();

            AccessTokenResponse tokenResponse =
                    keycloak.tokenManager().getAccessToken();

            LoginResponse response = new LoginResponse();
            response.setJwtToken(tokenResponse.getToken());
            response.setUsername(request.getUserName());
            response.setOrgCode("AVISYS");
            response.setDivision("ADMIN");
            response.setRole("ADMIN");

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Invalid username or password");
        }
    }
}
