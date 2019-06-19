package com.littlefox.model;

import com.littlefox.annotation.CrypticField;
import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @CrypticField(type = CrypticField.Type.ENCRYPT)
    private String id;
    @CrypticField
    private String userName;
    @CrypticField
    private String phone;
}
