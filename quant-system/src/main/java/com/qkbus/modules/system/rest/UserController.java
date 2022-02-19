
package com.qkbus.modules.system.rest;

import com.qkbus.aop.ForbidSubmit;
import com.qkbus.dozer.service.IGenerator;
import com.qkbus.google.GoogleCheck;
import com.qkbus.logging.aop.log.Log;
import com.qkbus.modules.system.domain.User;
import com.qkbus.modules.system.domain.vo.UserPassVo;
import com.qkbus.modules.system.service.DeptService;
import com.qkbus.modules.system.service.UserService;
import com.qkbus.modules.system.service.dto.UserDto;
import com.qkbus.modules.system.service.dto.UserQueryCriteria;
import com.qkbus.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 少林一枝花
 * @date 2021-11-23
 */
@Api(tags = "系统：用户管理")
@RestController
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;
    private final DeptService deptService;
    private final IGenerator generator;

    public UserController(UserService userService, DeptService deptService, IGenerator generator) {
        this.userService = userService;
        this.deptService = deptService;
        this.generator = generator;
    }

    @Log("导出用户数据")
    @ApiOperation("导出用户数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','user:list')")
    public void download(HttpServletResponse response, UserQueryCriteria criteria) throws IOException {
        userService.download(generator.convert(userService.queryAll(criteria), UserDto.class), response);
    }

    @Log("查询用户")
    @ApiOperation("查询用户")
    @GetMapping
    @PreAuthorize("@el.check('admin','user:list')")
    public ResponseEntity<Object> getUsers(UserQueryCriteria criteria, Pageable pageable) {
        Set<Long> deptSet = new HashSet<>();
        Set<Long> result = new HashSet<>();
        if (!ObjectUtils.isEmpty(criteria.getDeptId())) {
            deptSet.add(criteria.getDeptId());
            deptSet.addAll(deptService.getDeptChildren(deptService.findByPid(criteria.getDeptId())));
        }
        // 数据权限
        Set<Long> deptIds = deptService.getDeptIds();
        // 查询条件不为空并且数据权限不为空则取交集
        if (!CollectionUtils.isEmpty(deptIds) && !CollectionUtils.isEmpty(deptSet)) {
            // 取交集
            result.addAll(deptSet);
            result.retainAll(deptIds);
            // 若无交集，则代表无数据权限
            criteria.setDeptIds(result);
            if (result.size() == 0) {
                return new ResponseEntity<>(PageUtil.toPage(null, 0), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(userService.queryAll(criteria, pageable), HttpStatus.OK);
            }
            // 否则取并集
        } else {
            result.addAll(deptSet);
            result.addAll(deptIds);
            criteria.setDeptIds(result);
            return new ResponseEntity<>(userService.queryAll(criteria, pageable), HttpStatus.OK);
        }
    }

    @ForbidSubmit
    @Log("新增用户")
    @ApiOperation("新增用户")
    @PostMapping
    @PreAuthorize("@el.check('admin','user:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody User resources) {
        return new ResponseEntity<>(userService.create(resources), HttpStatus.CREATED);
    }

    @ForbidSubmit
    @Log("修改用户")
    @ApiOperation("修改用户")
    @PutMapping
    @PreAuthorize("@el.check('admin','user:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody User resources) {
        userService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ForbidSubmit
    @Log("修改用户：个人中心")
    @ApiOperation("修改用户：个人中心")
    @PutMapping(value = "center")
    public ResponseEntity<Object> center(@RequestBody User resources) {
        userService.updateCenter(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ForbidSubmit
    @Log("删除用户")
    @ApiOperation("删除用户")
    @DeleteMapping
    @PreAuthorize("@el.check('admin','user:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids) {
        userService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ForbidSubmit
    @ApiOperation("修改密码")
    @PostMapping(value = "/updatePass")
    public ResponseEntity<Object> updatePass(@RequestBody UserPassVo passVo) {
        userService.updatePass(passVo.getOldPass(), passVo.getNewPass());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ForbidSubmit
    @ApiOperation("修改头像")
    @PostMapping(value = "/updateAvatar")
    public ResponseEntity<Object> updateAvatar(@RequestParam MultipartFile file) {
        userService.updateAvatar(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ForbidSubmit
    @Log("修改邮箱")
    @ApiOperation("修改邮箱")
    @PostMapping(value = "/updateEmail/{code}")
    public ResponseEntity<Object> updateEmail(@PathVariable String code, @RequestBody User user) {
        userService.updateEmail(code, user.getPassword(), user.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/makeGoogleAuthKey")
    @ApiOperation("生成谷歌验证码")
    public ResponseEntity<Object> makeGoogleAuthKey() {
        return new ResponseEntity<>(GoogleCheck.createGoogleAuthKey(), HttpStatus.OK);
    }

}
