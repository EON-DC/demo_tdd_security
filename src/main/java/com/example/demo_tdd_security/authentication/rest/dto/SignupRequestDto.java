package com.example.demo_tdd_security.authentication.rest.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequestDto {

    private String email;
    private String password;
}
