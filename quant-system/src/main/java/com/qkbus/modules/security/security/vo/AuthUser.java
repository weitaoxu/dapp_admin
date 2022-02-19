
package com.qkbus.modules.security.security.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author 少林一枝花
 * @date 2021-11-30
 */
@Getter
@Setter
public class AuthUser {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String uuid = "";

    private String code;

    private Integer googleCode;

    @Override
    public String toString() {
        return "{username=" + username + ", password= ******}";
    }
}
