package com.baerchen.central.authentication.oauth.registeredclient.control;

import com.baerchen.central.authentication.oauth.registeredclient.boundary.RegisteredClientDTO;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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

    public List<RegisteredClientDTO> listAllClients() {
        return jdbcTemplate.query(
                "SELECT id, client_id, client_secret FROM oauth2_registered_client",
                (rs, rowNum) -> new RegisteredClientDTO(
                        rs.getString("id"),
                        rs.getString("client_id"),
                        rs.getString("client_secret"),
                        Set.of(), // TODO: parse redirect_uris/scopes if needed
                        Set.of(),
                        Set.of()
                )
        );
    }
}
