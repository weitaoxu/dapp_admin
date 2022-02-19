
package com.qkbus.modules.app.rest;

import com.qkbus.dozer.service.IGenerator;
import com.qkbus.logging.aop.log.Log;
import com.qkbus.modules.app.domain.AppVersion;
import com.qkbus.modules.app.service.AppVersionService;
import com.qkbus.modules.app.service.dto.AppVersionDto;
import com.qkbus.modules.app.service.dto.AppVersionQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author 少林一枝花
 * @date 2021-05-13
 */
@AllArgsConstructor
@Api(tags = "APP管理管理")
@RestController
@RequestMapping("/api/AppVersion")
public class AppVersionController {

    private final AppVersionService AppVersionService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','AppVersion:list')")
    public void download(HttpServletResponse response, AppVersionQueryCriteria criteria) throws IOException {
        AppVersionService.download(generator.convert(AppVersionService.queryAll(criteria), AppVersionDto.class), response);
    }

    @GetMapping
    @Log("查询APP管理")
    @ApiOperation("查询APP管理")
    @PreAuthorize("@el.check('admin','AppVersion:list')")
    public ResponseEntity<Object> getAppVersions(AppVersionQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(AppVersionService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增APP管理")
    @ApiOperation("新增APP管理")
    @PreAuthorize("@el.check('admin','AppVersion:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody AppVersion resources) {
        AppVersionService.create(resources);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    @Log("修改APP管理")
    @ApiOperation("修改APP管理")
    @PreAuthorize("@el.check('admin','AppVersion:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody AppVersion resources) {
        AppVersionService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除APP管理")
    @ApiOperation("删除APP管理")
    @PreAuthorize("@el.check('admin','AppVersion:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Set<Long> ids) {
        AppVersionService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}