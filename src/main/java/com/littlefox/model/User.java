package com.littlefox.model;

import com.littlefox.annotation.CrypticField;
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
    @CrypticField(type = CrypticField.Type.ONLY_ENCRYPT)
    private String id;

    private String userName;
    @CrypticField
    private String phone;

}
