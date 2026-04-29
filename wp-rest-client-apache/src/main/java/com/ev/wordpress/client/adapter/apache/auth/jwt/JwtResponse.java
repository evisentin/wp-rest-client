package com.ev.wordpress.client.adapter.apache.auth.jwt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtResponse {

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("jwt_token")
    private String jwtToken;

    @JsonProperty("iat")
    private long iat;

    @JsonProperty("expires_in")
    private long expiresIn;
}
