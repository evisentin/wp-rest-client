package io.github.evisentin.wordpress.rest.client.domain.auth.jwt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO representing the response returned by a JWT token endpoint.
 *
 * <p>Unknown JSON properties are ignored.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtResponse {
    @JsonProperty("jwt_token")
    private String token;
}
