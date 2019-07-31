package com.littlefox.example.model;

import com.littlefox.security.annotation.CrypticField;
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
    @CrypticField(type = CrypticField.Type.ONLY_ENCRYPT)
    private String lid;
    @CrypticField(type = CrypticField.Type.ONLY_ENCRYPT)
    private String age;
    @CrypticField
    private String address;
}
