package com.jwt.security.dto.user;

import com.jwt.security.domain.User;
import lombok.Data;

@Data
public class UserToken {

    private Long id;
    private String email;
    private UserRole role;
}
