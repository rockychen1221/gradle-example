package com.littlefox.example.model;

import com.littlefox.security.annotation.Cryptic;
import lombok.*;

/**
 * 角色
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Cryptic
    private String roleId;

    private String userId;

    private String roleName;
}
