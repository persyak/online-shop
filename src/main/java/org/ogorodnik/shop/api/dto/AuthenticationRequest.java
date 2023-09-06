package org.ogorodnik.shop.api.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    private String username;
    private String password;
}
