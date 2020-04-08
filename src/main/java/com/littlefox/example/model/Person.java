package com.littlefox.example.model;

import com.littlefox.security.annotation.Cryptic;
import lombok.*;

/**
 * 用户
 * @author rockychen
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Cryptic
    private String lid;
    @Cryptic
    private String age;
    @Cryptic
    private String address;
}
