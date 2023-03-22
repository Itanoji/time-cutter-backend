package com.itanoji.time.cutter.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class UserPojo implements Serializable {
    String login;
    String email;
    String password;
}
