package com.baerchen.central.authentication.oauth.registeredclient.boundary;

import com.baerchen.central.authentication.fixtures.JwtTestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenJwtWithAdminRole_thenCanAccessAdminEndpoint() throws Exception {
        // Build a JWT token with "roles": ["ADMIN"]
        String token = JwtTestUtils.generateJwtWithRoles(List.of("ADMIN"));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/hello")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, Admin!"));
    }

    @Test
    void whenJwtWithUserRole_thenForbidden() throws Exception {
        String token = JwtTestUtils.generateJwtWithRoles(List.of("USER"));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/hello")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

}