package com.baerchen.central.authentication.registeredclient.boundary;

import com.baerchen.central.authentication.runtime.entity.Defaults;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RegisteredClientMapper extends Defaults {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "clientId", target = "clientId")
    @Mapping(source = "clientSecret", target = "clientSecret")
    @Mapping(source = "scopes", target = "scopes")
    @Mapping(source = "redirectUris", target = "redirectUris")
    @Mapping(expression = "java(toGrantTypeSet(client.getAuthorizationGrantTypes()))", target = "grantTypes" )
    @Mapping(expression = "java(toAuthenticationMethodSet(client.getClientAuthenticationMethods()))", target = "authenticationMethods")
    @Mapping(target = "clientSettings", expression = "java(client.getClientSettings().getSettings())")
    RegisteredClientDTO toDto(RegisteredClient client);

    default RegisteredClient toEntity(RegisteredClientDTO dto, @Context PasswordEncoder encoder ) {
        RegisteredClientDTO finalDto =  withDefaults(dto);
        return RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(dto.clientId())
                .clientSecret(encoder.encode(dto.clientSecret()))
                .redirectUris(uris -> uris.addAll(finalDto.redirectUris()))
                .scopes(scopes -> scopes.addAll(finalDto.scopes()))
                .authorizationGrantTypes(grants -> finalDto.grantTypes().forEach(grant ->
                        grants.add(new AuthorizationGrantType(grant))))
                .clientAuthenticationMethods(methods -> finalDto.authenticationMethods().forEach(method ->
                        methods.add(new ClientAuthenticationMethod(method))))
                .build();
    }

    default RegisteredClient toEntityForUpdate(RegisteredClientDTO dto, @Context PasswordEncoder encoder ) {
        var builder =  RegisteredClient.withId(dto.id())
                .clientId(dto.clientId())
                .clientSecret(dto.clientSecret()!=null ? encoder.encode(dto.clientSecret()): null)
                .redirectUris(uris -> uris.addAll(dto.redirectUris()))
                .scopes(scopes -> scopes.addAll(dto.scopes()))
                .authorizationGrantTypes(grants -> dto.grantTypes().forEach(grant ->
                        grants.add(new AuthorizationGrantType(grant))))
                .clientAuthenticationMethods(methods -> dto.authenticationMethods().forEach(method ->
                        methods.add(new ClientAuthenticationMethod(method))));
        if (dto.clientSettings() != null) {
            builder.clientSettings(ClientSettings.withSettings(dto.clientSettings()).build());
        }
        return builder.build();

    }


    /**
     * those could be this one
     *     default <T> Set<String> toSet(Set<T> input, Function<T, String> stringMapper) {
     *         return input == null ? Set.of() : input.stream().map(stringMapper).collect(Collectors.toSet());
     *     }*
     */
    default Set<String> toAuthenticationMethodSet(Set<ClientAuthenticationMethod> input) {return input == null ? Set.of() : input.stream().map(ClientAuthenticationMethod::getValue).collect(Collectors.toSet());};

    default Set<String> toGrantTypeSet(Set<AuthorizationGrantType> input) {return input == null ? Set.of() : input.stream().map(AuthorizationGrantType::getValue).collect(Collectors.toSet());};

    default RegisteredClientDTO withDefaults(RegisteredClientDTO dto) {
        return new RegisteredClientDTO(
                dto.id(),
                dto.clientId(),
                dto.clientSecret(),
                defaultSet(dto.redirectUris(), DEFAULT_REDIRECT_URIS),
                defaultSet(dto.scopes(), DEFAULT_SCOPES),
                defaultSet(dto.authenticationMethods(), DEFAULT_AUTH_METHODS),
                defaultSet(dto.grantTypes(), DEFAULT_GRANTS),
                dto.clientSettings()
        );
    }
}
