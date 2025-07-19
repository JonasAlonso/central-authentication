package com.baerchen.central.authentication.registeredclient.control;

import com.baerchen.central.authentication.registeredclient.boundary.RegisteredClientDTO;
import com.baerchen.central.authentication.registeredclient.boundary.RegisteredClientMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(CustomRegisteredClientRepo.class)
@ComponentScan(basePackages = {"com.baerchen.central.authentication"})
class CustomRegisteredClientRepoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CustomRegisteredClientRepo repo;

    @BeforeEach
    void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
                new ClassPathResource("db/sql/oauth2_registered_client.sql"));
        populator.execute(jdbcTemplate.getDataSource());
        jdbcTemplate.update(
                "INSERT INTO oauth2_registered_client(id, client_id, client_name, client_authentication_methods, authorization_grant_types, redirect_uris, scopes, client_settings, token_settings) VALUES (?,?,?,?,?,?,?,?,?)",
                "1", "test-client", "Test Client",
                "client_secret_basic, client_secret_post",
                "authorization_code, refresh_token",
                "https://a.com, https://b.com",
                "read, write", "{}", "{}");
    }

    @Test
    void listAllClientsParsesCommaSeparatedValues() {
        List<RegisteredClientDTO> clients = repo.listAllClients();
        assertThat(clients).hasSize(1);
        RegisteredClientDTO dto = clients.get(0);
        assertThat(dto.redirectUris()).containsExactlyInAnyOrder("https://a.com", "https://b.com");
        assertThat(dto.scopes()).containsExactlyInAnyOrder("read", "write");
        assertThat(dto.authenticationMethods()).containsExactlyInAnyOrder("client_secret_basic", "client_secret_post");
        assertThat(dto.grantTypes()).containsExactlyInAnyOrder("authorization_code", "refresh_token");
    }
}
