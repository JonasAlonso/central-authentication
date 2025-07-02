package com.baerchen.central.authentication.fixtures;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class JwtTestUtils {

    // Use a fixed test secret (must match your resource server config)

    public static String generateJwtWithRoles(List<String> roles) throws Exception {
        RSAPrivateKey privateKey = (RSAPrivateKey) PemUtils.readPrivateKey("src/test/resources/private.pem");

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject("testuser")
                .issuer("http://localhost:9000")
                .expirationTime(Date.from(Instant.now().plusSeconds(3600)))
                .claim("roles", roles)
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.RS256),
                claims
        );

        signedJWT.sign(new RSASSASigner(privateKey));
        return signedJWT.serialize();
    }
}
