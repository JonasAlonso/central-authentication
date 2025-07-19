package com.baerchen.central.authentication.registeredclient.control;

import com.baerchen.central.authentication.registeredclient.boundary.RegisteredClientDTO;
import com.baerchen.central.authentication.runtime.control.Parser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@AllArgsConstructor
public class CustomRegisteredClientRepo implements Parser {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public void deleteClientById(String id) {
        this.jdbcTemplate.update("DELETE FROM oauth2_registered_client WHERE id = ?", id);
    }

    public void deleteClientByClientId(String clientId) {
        this.jdbcTemplate.update("DELETE FROM oauth2_registered_client WHERE client_id = ?", clientId);
    }

    public void updateClientById(RegisteredClient client){
       var sql = "UPDATE oauth2_registered_client SET client_authentication_methods = ?, authorization_grant_types = ?, redirect_uris = ?, scopes = ?, client_settings = ? WHERE id = ?";
        this.jdbcTemplate.update(sql,
                parse(client.getClientAuthenticationMethods(), ClientAuthenticationMethod::getValue),
                parse(client.getAuthorizationGrantTypes(), AuthorizationGrantType::getValue),
                parse(client.getRedirectUris(), String::valueOf),
                parse(client.getScopes(), String::valueOf),
                convertToDatabaseColumn(client.getClientSettings().getSettings()),
                client.getId());

    }

    public void updateClientByClientId(RegisteredClient client) {
        var sql = "UPDATE oauth2_registered_client SET client_authentication_methods = ?, authorization_grant_types = ?, redirect_uris = ?, scopes = ?, client_settings = ? , client_secret = ? WHERE client_id = ?";
        this.jdbcTemplate.update(sql, parse(client.getClientAuthenticationMethods(),ClientAuthenticationMethod::getValue), parse(client.getAuthorizationGrantTypes(), AuthorizationGrantType::getValue), parse(client.getRedirectUris(), t -> ""+t), parse(client.getScopes(),t -> ""+t), convertToDatabaseColumn(client.getClientSettings().getSettings()), client.getClientSecret(), client.getClientId());
    }

    public List<RegisteredClientDTO> listAllClients() {
        return jdbcTemplate.query(
                "SELECT id, client_id, client_secret, redirect_uris, scopes, client_authentication_methods, authorization_grant_types FROM oauth2_registered_client",
                (rs, rowNum) -> new RegisteredClientDTO(
                        rs.getString("id"),
                        rs.getString("client_id"),
                        rs.getString("client_secret"),
                        parseSet(rs.getString("redirect_uris")),
                        parseSet(rs.getString("scopes")),
                        parseSet(rs.getString("client_authentication_methods")),
                        parseSet(rs.getString("authorization_grant_types")),
                        Map.of() //TO-DO implement parseMap
        ));
    }

    public  String convertToDatabaseColumn(Map<String, Object> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not serialize clientSettings", e);
        }
    }
}
