package com.baerchen.central.authentication.registeredclient.control;

import com.baerchen.central.authentication.registeredclient.boundary.RegisteredClientDTO;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Component
public class RegisteredClientValidator {

      public void validate(RegisteredClientDTO dto) {
          if (dto.authenticationMethods().contains("none") && StringUtils.hasText(dto.clientSecret())) {
              throw new IllegalArgumentException("Public clients (auth method 'none') cannot have a client secret.");
         }
          Object value = dto.clientSettings().get("settings.client.require-proof-key");

          if (dto.grantTypes().contains("authorization_code") && !(value instanceof Boolean && (Boolean) value)) {
              throw new IllegalArgumentException("Authorization code flow with public clients must require PKCE.");
          }
      }

}
