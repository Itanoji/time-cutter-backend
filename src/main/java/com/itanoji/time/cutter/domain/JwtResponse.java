package com.itanoji.time.cutter.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    String accessToken;
    String refreshToken;
}
