package com.baerchen.central.authentication.config.spring;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;

@Configuration
public class BeanConfig {

    @Bean("objectMapper")
    @Primary
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();


        objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
        objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL
        );

        /**
         * registering this module allows jackson to deserialize links by springframework.hateoas:
         *
         * {
         *   "_links": {
         *     "self": {
         *       "href": "http://localhost:9090/..."
         *     }
         *   }
         * }
        */
        objectMapper.registerModule(new Jackson2HalModule());

        return objectMapper;
    }

    @Bean("polymorphicMapper")
    public ObjectMapper polymorphicMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );
        return mapper;
    }

}
