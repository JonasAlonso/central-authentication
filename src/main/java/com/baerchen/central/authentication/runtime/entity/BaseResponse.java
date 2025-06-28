package com.baerchen.central.authentication.runtime.entity;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,                // Use the "type" property to determine the subtype
        include = JsonTypeInfo.As.PROPERTY,        // Include it as a field in JSON
        property = "type",                         // JSON must contain: "type": "payment"
        visible = true                             // Keep the "type" property in the final object
)
@JsonSubTypes({
     //   @JsonSubTypes.Type(value = RegisterRequest.class, name = "register"),
})
public abstract class BaseResponse {
    private String type;

    public String getType() {
        return type;
    }
}
