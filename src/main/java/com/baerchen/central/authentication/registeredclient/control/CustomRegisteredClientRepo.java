package com.baerchen.central.authentication.registeredclient.control;

import com.baerchen.central.authentication.registeredclient.boundary.RegisteredClientDTO;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomRegisteredClientRepo {

    private final JdbcTemplate jdbcTemplate;

    public void deleteClientById(String id) {
        this.jdbcTemplate.update("DELETE FROM oauth2_registered_client WHERE id = ?", id);
    }

    public void deleteClientByClientId(String clientId) {
        this.jdbcTemplate.update("DELETE FROM oauth2_registered_client WHERE client_id = ?", clientId);
    }

    public void updateClientById(RegisteredClient client){
       var sql = "UPDATE oauth2_registered_client SET client_authentication_methods = ?, authorization_grant_types = ?, redirect_uris = ?, scopes = ? WHERE id = ?";
        this.jdbcTemplate.update(sql, client.getClientAuthenticationMethods(), client.getAuthorizationGrantTypes(), client.getRedirectUris(), client.getScopes(), client.getId());
    }

    public void updateClientByClientId(RegisteredClient client) {
        var sql = "UPDATE oauth2_registered_client SET client_authentication_methods = ?, authorization_grant_types = ?, redirect_uris = ?, scopes = ? WHERE client_id = ?";
        this.jdbcTemplate.update(sql, parse(client.getClientAuthenticationMethods(),ClientAuthenticationMethod::getValue), parse(client.getAuthorizationGrantTypes(), AuthorizationGrantType::getValue), parse(client.getRedirectUris(), t -> ""+t), parse(client.getScopes(),t -> ""+t), client.getClientId());
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
                        parseSet(rs.getString("authorization_grant_types"))
        ));
    }

    private Set<String> parseSet(String input) {
        if (input == null || input.isBlank()) {
            return Set.of();
        }
        return Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }


    private <T> String parse(Set<T> input, Function<T, String> stringMapper) {
        if (input == null || input.isEmpty()) {
            return Strings.EMPTY;
        }
        return input.stream()
                .map(stringMapper)
                .collect(Collectors.joining(","));
    }


    /**
     *     private Set<String> redirectUris;
     *     private Set<String> postLogoutRedirectUris;
     *     private Set<String> scopes;
     */

    /**
     * public record RegisteredClientDTO(
     *         String id,
     *         String clientId,
     *         String clientSecret,
     *         Set<String> redirectUris,
     *         Set<String> scopes,
     *         Set<String> authenticationMethods,
     *         Set<String> grantTypes
     * ) {}
     *
     *
     *     client_authentication_methods varchar(1000) NOT NULL,
     *     authorization_grant_types varchar(1000) NOT NULL,
     *     redirect_uris varchar(1000) DEFAULT NULL,
     *     post_logout_redirect_uris varchar(1000) DEFAULT NULL,
     */
}
