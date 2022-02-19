
package com.qkbus.modules.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.qkbus.common.service.impl.BaseServiceImpl;
import com.qkbus.common.utils.QueryHelpPlus;
import com.qkbus.config.FileProperties;
import com.qkbus.dozer.service.IGenerator;
import com.qkbus.enums.CodeEnum;
import com.qkbus.exception.BadRequestException;
import com.qkbus.exception.EntityExistException;
import com.qkbus.modules.system.domain.Role;
import com.qkbus.modules.system.domain.User;
import com.qkbus.modules.system.domain.UsersRoles;
import com.qkbus.modules.system.service.RoleService;
import com.qkbus.modules.system.service.UserService;
import com.qkbus.modules.system.service.UsersRolesService;
import com.qkbus.modules.system.service.dto.RoleSmallDto;
import com.qkbus.modules.system.service.dto.UserDto;
import com.qkbus.modules.system.service.dto.UserQueryCriteria;
import com.qkbus.modules.system.service.mapper.DeptMapper;
import com.qkbus.modules.system.service.mapper.JobMapper;
import com.qkbus.modules.system.service.mapper.RoleMapper;
import com.qkbus.modules.system.service.mapper.SysUserMapper;
import com.qkbus.tools.service.VerificationCodeService;
import com.qkbus.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author 少林一枝花
 * @date 2020-05-14
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@CacheConfig(cacheNames = "user")
@Slf4j
public class UserServiceImpl extends BaseServiceImpl<SysUserMapper, User> implements UserService {

    @Value("${rsa.private_key}")
    private String privateKey;
    private final IGenerator generator;
    private final JobMapper jobMapper;
    private final DeptMapper deptMapper;
    private final RoleMapper roleMapper;
    private final RoleService roleService;
    private final RedisUtils redisUtils;
    private final FileProperties properties;
    private final UsersRolesService usersRolesService;
    private final FileProperties fileProperties;
    private final PasswordEncoder passwordEncoder;
    private final VerificationCodeService verificationCodeService;

    @Override
    public Map<String, Object> queryAll(UserQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<User> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), UserDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    public List<User> queryAll(UserQueryCriteria criteria) {
        List<User> userList = this.list(QueryHelpPlus.getPredicate(User.class, criteria));
        for (User user : userList) {
            user.setJob(jobMapper.selectById(user.getJobId()));
            user.setDept(deptMapper.selectById(user.getDeptId()));
            user.setRoles(roleMapper.findByUsers_Id(user.getId()));
        }
        return userList;
    }


    @Override
    public void download(List<UserDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UserDto user : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("邮箱", user.getEmail());
            map.put("状态", user.getEnabled());
            map.put("密码", user.getPassword());
            map.put("用户名", user.getUsername());
            map.put("部门名称", user.getDeptId());
            map.put("手机号码", user.getPhone());
            map.put("创建日期", user.getGmtCreate());
            map.put("最后修改密码的日期", user.getLastPasswordResetTime());
            map.put("昵称", user.getNickName());
            map.put("性别", user.getSex());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 根据用户名查询
     *
     * @param userName /
     * @return /
     */
    @Override
    @Cacheable(key = "'info:' + #p0")
    public UserDto findByName(String userName) {
        User user = this.query().eq("username", userName).one();
        if (Objects.isNull(user)) {
            throw new BadRequestException("用户不存在");
        }
        //用户所属岗位
        user.setJob(jobMapper.selectById(user.getJobId()));
        //用户所属部门
        user.setDept(deptMapper.selectById(user.getDeptId()));
        return generator.convert(user, UserDto.class);
    }

    /**
     * 修改密码
     *
     * @param oldPwd 用户名
     * @param newPwd 密码
     */
    @Override
    @CacheEvict(value = {"user"}, allEntries = true)
    public void updatePass(String oldPwd, String newPwd) {
        // 密码解密
        String oldPass = RsaUtils.decryptByPrivateKey(privateKey, oldPwd);
        String newPass = RsaUtils.decryptByPrivateKey(privateKey, newPwd);
        User user = this.getById(SecurityUtils.getUserId());
        if (!passwordEncoder.matches(oldPass, user.getPassword())) {
            throw new BadRequestException("修改失败，旧密码错误");
        }
        if (passwordEncoder.matches(newPass, user.getPassword())) {
            throw new BadRequestException("新密码不能与旧密码相同");
        }
        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setPassword(passwordEncoder.encode(newPass));
        newUser.setLastPasswordResetTime(new Timestamp(new Date().getTime()));
        this.updateById(newUser);
    }

    /**
     * 修改头像
     *
     * @param multipartFile 文件
     */
    @Override
    @CacheEvict(value = {"user"}, allEntries = true)
    public Map<String, String> updateAvatar(MultipartFile multipartFile) {
        // 文件大小验证
        FileUtil.checkSize(properties.getAvatarMaxSize(), multipartFile.getSize());
        try {
            boolean image = HelpUtils.isImage(multipartFile.getInputStream());
            if (!image) {
                throw new BadRequestException("文件格式错误！, 请上传正确的图片格式格式");
            }
        } catch (Exception e) {
            log.error("修改头像上传图片失败：" + e.getMessage());
            e.printStackTrace();
        }
        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, SecurityUtils.getUsername()));
        String oldPath = user.getAvatarPath();
        File file = FileUtil.upload(multipartFile, fileProperties.getPath().getAvatar());
        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setAvatarPath(Objects.requireNonNull(file).getPath());
        newUser.setAvatar(file.getName());
        this.updateById(newUser);
        if (StringUtils.isNotBlank(oldPath)) {
            FileUtil.del(oldPath);
        }
        return new HashMap<String, String>(1) {{
            put("avatar", file.getName());
        }};
    }

    /**
     * 修改邮箱
     *
     * @param code  用户名
     * @param email 邮箱
     */
    @Override
    @CacheEvict(value = {"user"}, allEntries = true)
    public void updateEmail(String code, String password, String email) {
        String newPass = RsaUtils.decryptByPrivateKey(privateKey, password);
        User user = this.getById(SecurityUtils.getUserId());
        if (!passwordEncoder.matches(newPass, user.getPassword())) {
            throw new BadRequestException("密码错误");
        }
        verificationCodeService.validated(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + user.getEmail(), code);
        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setEmail(email);
        this.updateById(newUser);
    }

    /**
     * 新增用户
     *
     * @param resources /
     * @return /
     */
    @Override
    public boolean create(User resources) {
        this.checkLevel(resources.getRoles());
        // 默认密码 123456
        resources.setPassword(passwordEncoder.encode("123456"));
        User userName = this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, resources.getUsername()));
        if (userName != null) {
            throw new EntityExistException(User.class, "username", resources.getUsername());
        }
        User userEmail = this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, resources.getEmail()));
        if (userEmail != null) {
            throw new EntityExistException(User.class, "email", resources.getEmail());
        }
        resources.setDeptId(resources.getDept().getId());
        resources.setJobId(resources.getJob().getId());
        boolean result = this.save(resources);
        if (result) {
            usersRolesService.saveUserRole(resources.getId(), resources.getRoles());
        }
        return result;
    }

    /**
     * 编辑用户
     *
     * @param resources /
     */
    @Override
    @CacheEvict(value = {"menu", "role", "userRole", "user"}, allEntries = true)
    public void update(User resources) {
        this.checkLevel(resources.getRoles());
        User user = this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, resources.getId()));
        ValidationUtil.isNull(user.getId(), "User", "id", resources.getId());
        User userName = this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, resources.getUsername()));
        User userEmail = this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, resources.getEmail()));
        User userPhone = this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getPhone, resources.getPhone()));

        if (userName != null && !user.getId().equals(userName.getId())) {
            throw new BadRequestException("当前用户名已存在");
        }

        if (userEmail != null && !user.getId().equals(userEmail.getId())) {
            throw new EntityExistException(User.class, "email", resources.getEmail());
        }
        if (userPhone != null && !user.getId().equals(userPhone.getId())) {
            throw new EntityExistException(User.class, "email", resources.getEmail());
        }
        user.setUsername(resources.getUsername());
        user.setEmail(resources.getEmail());
        user.setEnabled(resources.getEnabled());
        user.setDeptId(resources.getDept().getId());
        user.setJobId(resources.getJob().getId());
        user.setPhone(resources.getPhone());
        user.setNickName(resources.getNickName());
        user.setSex(resources.getSex());
        if (this.saveOrUpdate(user)) {
            usersRolesService.saveUserRole(resources.getId(), resources.getRoles());
        }
    }

    @Override
    @CacheEvict(value = {"user"}, allEntries = true)
    public void updateCenter(User resources) {
        User userDto = this.getById(SecurityUtils.getUserId());
        if (!resources.getId().equals(userDto.getId())) {
            throw new BadRequestException("不能修改他人资料");
        }
        this.updateById(resources);
    }

    @Override
    @CacheEvict(value = {"menu", "role", "userRole"}, allEntries = true)
    public void delete(Set<Long> ids) {
        Integer levels = usersRolesService.getLevels(null);
        for (Long id : ids) {
            List<RoleSmallDto> roleByUsersId = usersRolesService.findRoleByUsersId(id);
            Integer optLevel = Collections.min(roleByUsersId.stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
            if (levels > optLevel) {
                throw new BadRequestException("角色权限不足，不能删除：" + SecurityUtils.getUsername());
            }
        }
        for (Long id : ids) {
            usersRolesService.lambdaUpdate().eq(UsersRoles::getUserId, id).remove();
        }
        this.removeByIds(ids);
    }

    /**
     * 如果当前用户的角色级别低于创建用户的角色级别，则抛出权限不足的错误
     *
     * @param checkRoles
     */
    @Override
    public void checkLevel(Set<Role> checkRoles) {
        List<RoleSmallDto> roles = usersRolesService.findRoleByUsersId(SecurityUtils.getUserId());
        Integer currentLevel = Collections.min(roles.stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
        Integer optLevel = roleService.findLevelByRoles(checkRoles);
        if (ObjectUtil.isNull(optLevel) || currentLevel > optLevel) {
            throw new BadRequestException("角色权限不足");
        }
    }
}
