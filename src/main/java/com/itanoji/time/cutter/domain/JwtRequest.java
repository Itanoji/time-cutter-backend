package com.itanoji.time.cutter.domain;

import lombok.Data;

@Data
public class JwtRequest {
    String login;
    String password;
}
