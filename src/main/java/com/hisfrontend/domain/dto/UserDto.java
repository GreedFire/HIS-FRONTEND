package com.hisfrontend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private long id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private boolean signedIn;

}