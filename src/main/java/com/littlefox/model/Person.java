package com.littlefox.model;

import com.littlefox.annotation.CrypticField;
import lombok.*;

/**
 * 用户
 * @author rockychen
 * @date
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