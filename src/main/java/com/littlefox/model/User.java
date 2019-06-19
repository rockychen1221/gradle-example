package com.littlefox.model;

import com.littlefox.annotation.CrypticField;
import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @CrypticField(type = CrypticField.Type.ONLY_ENCRYPT)
    private String id;
    @CrypticField
    private String userName;
    @CrypticField(type = CrypticField.Type.ONLY_ENCRYPT)
    private String phone;
}
