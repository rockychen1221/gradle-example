package com.littlefox.example.model;

import com.littlefox.security.annotation.Cryptic;
import lombok.*;

import java.util.List;

/**
 * 用户
 * @author rockychen
 */
@Data
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends Person{

    @Cryptic
    private String id;

    private String userName;

    @Cryptic
    private String phone;

    private List<Role> roles;

}
