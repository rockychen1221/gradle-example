package com.littlefox.model;

import com.littlefox.annotation.CrypticField;
import lombok.*;

import java.util.List;

/**
 * 用户
 * @author rockychen
 * @date
 */
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
    @CrypticField
    private String phone;

    @CrypticField
    private List<String> list;
}
