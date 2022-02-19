
package com.qkbus.modules.system.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qkbus.common.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

/**
 * @author 少林一枝花
 * @date 2020-05-14
 */
@Data
@TableName("sys_user")
public class User extends BaseEntity {

    /**
     * 是否是超级账号 1是 0不是
     */
    public static final Integer IS_ADMIN_YES = 1;
    public static final Integer IS_ADMIN_NO = 0;


    /**
     * 系统用户ID
     */
    @TableId
    private Long id;


    /**
     * 头像
     */
    private String avatar;

    /**
     * 头像 路径
     */
    private String avatarPath;


    /**
     * 邮箱
     */
    private String email;


    /**
     * 状态：1启用、0禁用
     */
    private Boolean enabled;


    /**
     * 用户角色
     */
    @TableField(exist = false)
    private Set<Role> roles;

    /**
     * 用户职位
     */
    @TableField(exist = false)
    private Job job;

    /**
     * 用户部门
     */
    @TableField(exist = false)
    private Dept dept;

    /**
     * 密码
     */
    private String password;


    /**
     * 用户名
     */
    @NotBlank(message = "请填写用户名称")
    private String username;


    /**
     * 部门名称
     */
    private Long deptId;


    /**
     * 手机号码
     */
    @NotBlank(message = "请输入手机号码")
    private String phone;


    /**
     * 岗位名称
     */
    private Long jobId;


    /**
     * 最后修改密码的日期
     */
    private Timestamp lastPasswordResetTime;


    /**
     * 昵称
     */
    private String nickName;


    /**
     * 性别
     */
    private String sex;

    private Integer isAdmin;
    @ApiModelProperty(value = "谷歌验证码")
    private String googleKey;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    public void copy(User source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
