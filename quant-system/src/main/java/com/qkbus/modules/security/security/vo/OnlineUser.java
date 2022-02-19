
package com.qkbus.modules.security.security.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 少林一枝花
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUser {
    private Long id;
    private String userName;

    private String nickName;

    private String job;

    private String browser;

    private String ip;

    private String address;

    private String key;

    private Date loginTime;


}
