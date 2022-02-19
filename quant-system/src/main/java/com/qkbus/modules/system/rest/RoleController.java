
package com.qkbus.modules.system.rest;

import cn.hutool.core.lang.Dict;
import com.qkbus.aop.ForbidSubmit;
import com.qkbus.dozer.service.IGenerator;
import com.qkbus.logging.aop.log.Log;
import com.qkbus.modules.system.domain.Role;
import com.qkbus.modules.system.service.RoleService;
import com.qkbus.modules.system.service.UsersRolesService;
import com.qkbus.modules.system.service.dto.RoleDto;
import com.qkbus.modules.system.service.dto.RoleQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author 少林一枝花
 * @date 2021-12-03
 */
@Api(tags = "系统：角色管理")
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;
    private final IGenerator generator;
    private final UsersRolesService usersRolesService;

    public RoleController(RoleService roleService, UsersRolesService usersRolesService, IGenerator generator) {
        this.roleService = roleService;
        this.usersRolesService = usersRolesService;
        this.generator = generator;
    }

    @ApiOperation("获取单个role")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('roles:list')")
    public ResponseEntity<RoleDto> getRoles(@PathVariable Long id) {
        return new ResponseEntity<>(roleService.findById(id), HttpStatus.OK);
    }

    @Log("导出角色数据")
    @ApiOperation("导出角色数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('role:list')")
    public void download(HttpServletResponse response, RoleQueryCriteria criteria) throws IOException {
        roleService.download(generator.convert(roleService.queryAll(criteria), RoleDto.class), response);
    }

    @ApiOperation("返回全部的角色")
    @GetMapping(value = "/all")
    @PreAuthorize("@el.check('roles:list','user:add','user:edit')")
    public ResponseEntity<List<Role>> getAll(RoleQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(roleService.queryAlls(criteria, pageable), HttpStatus.OK);
    }

    @Log("查询角色")
    @ApiOperation("查询角色")
    @GetMapping
    @PreAuthorize("@el.check('roles:list')")
    public ResponseEntity<Object> getRoles(RoleQueryCriteria criteria, Pageable pageable) {

        return new ResponseEntity<>(roleService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @ApiOperation("获取用户级别")
    @GetMapping(value = "/level")
    public ResponseEntity<Object> getLevel() {
        return new ResponseEntity<>(Dict.create().set("level", usersRolesService.getLevels(null)), HttpStatus.OK);
    }

    @ForbidSubmit
    @Log("新增角色")
    @ApiOperation("新增角色")
    @PostMapping
    @PreAuthorize("@el.check('roles:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Role resources) {
        return new ResponseEntity<>(roleService.create(resources), HttpStatus.CREATED);
    }

    @ForbidSubmit
    @Log("修改角色")
    @ApiOperation("修改角色")
    @PutMapping
    @PreAuthorize("@el.check('roles:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Role resources) {
        roleService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ForbidSubmit
    @Log("修改角色菜单")
    @ApiOperation("修改角色菜单")
    @PutMapping(value = "/menu")
    @PreAuthorize("@el.check('roles:edit')")
    public ResponseEntity<Object> updateMenu(@RequestBody Role resources) {
        roleService.updateMenu(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ForbidSubmit
    @Log("删除角色")
    @ApiOperation("删除角色")
    @DeleteMapping
    @PreAuthorize("@el.check('roles:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids) {
        roleService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
