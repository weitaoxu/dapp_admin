
package com.qkbus.modules.system.service;

import com.qkbus.common.service.BaseService;
import com.qkbus.modules.system.domain.Role;
import com.qkbus.modules.system.domain.User;
import com.qkbus.modules.system.service.dto.UserDto;
import com.qkbus.modules.system.service.dto.UserQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 少林一枝花
 * @date 2020-05-14
 */
public interface UserService extends BaseService<User> {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(UserQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<UserDto>
     */
    List<User> queryAll(UserQueryCriteria criteria);

    /**
     * 导出数据
     *
     * @param all      待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<UserDto> all, HttpServletResponse response) throws IOException;

    /**
     * 根据用户名查询
     *
     * @param userName /
     * @return /
     */
    UserDto findByName(String userName);

    /**
     * 修改密码
     *
     * @param oldPwd 用户名
     * @param newPwd 密码
     */
    void updatePass(String oldPwd, String newPwd);

    /**
     * 修改头像
     *
     * @param multipartFile 文件
     */
    Map<String, String> updateAvatar(MultipartFile multipartFile);

    /**
     * 修改邮箱
     *
     * @param code
     * @param email 邮箱
     */
    void updateEmail(String code, String password, String email);

    /**
     * 新增用户
     *
     * @param resources /
     * @return /
     */
    boolean create(User resources);

    /**
     * 编辑用户
     *
     * @param resources /
     */
    void update(User resources);

    /*
     * 修改个人资料
     * */
    void updateCenter(User resources);

    void delete(Set<Long> ids);


    /**
     * 如果当前用户的角色级别低于创建用户的角色级别，则抛出权限不足的错误
     *
     * @param checkRoles
     */
    void checkLevel(Set<Role> checkRoles);
}
