package com.littlefox.model;

import com.littlefox.annotation.CrypticField;
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
    @CrypticField(type = CrypticField.Type.ONLY_ENCRYPT)
    private String roleId;

    private String roleName;
}
