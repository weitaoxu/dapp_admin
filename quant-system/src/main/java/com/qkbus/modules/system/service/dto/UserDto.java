
package com.qkbus.modules.system.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

/**
 * @author 少林一枝花
 * @date 2020-05-14
 */
@Data
public class UserDto implements Serializable {


    @ApiModelProperty(hidden = true)
    private Long id;

    private String username;

    private String nickName;

    private String sex;

    private String avatar;

    private String email;

    private String phone;

    private Boolean enabled;

    @JsonIgnore
    private String password;
    @JsonIgnore
    private Integer isAdmin;
    @JsonIgnore
    private String googleKey;


    private Timestamp lastPasswordResetTime;

    @ApiModelProperty(hidden = true)
    private Set<RoleSmallDto> roles;

    @ApiModelProperty(hidden = true)
    private JobSmallDto job;

    private DeptSmallDto dept;

    private Long deptId;

    private Timestamp gmtCreate;

}
