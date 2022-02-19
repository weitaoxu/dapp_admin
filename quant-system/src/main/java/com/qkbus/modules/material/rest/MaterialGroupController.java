
package com.qkbus.modules.material.rest;

import com.qkbus.dozer.service.IGenerator;
import com.qkbus.logging.aop.log.Log;
import com.qkbus.modules.material.domain.MaterialGroup;
import com.qkbus.modules.material.service.MaterialGroupService;
import com.qkbus.modules.material.service.dto.MaterialGroupDto;
import com.qkbus.modules.material.service.dto.MaterialGroupQueryCriteria;
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
 * @date 2021-05-11
 */
@AllArgsConstructor
@Api(tags = "资料分类管理")
@RestController
@RequestMapping("/api/MaterialGroup")
public class MaterialGroupController {

    private final MaterialGroupService MaterialGroupService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','MaterialGroup:list')")
    public void download(HttpServletResponse response, MaterialGroupQueryCriteria criteria) throws IOException {
        MaterialGroupService.download(generator.convert(MaterialGroupService.queryAll(criteria), MaterialGroupDto.class), response);
    }

    @GetMapping
    @Log("查询资料分类")
    @ApiOperation("查询资料分类")
    @PreAuthorize("@el.check('admin','MaterialGroup:list')")
    public ResponseEntity<Object> getMaterialGroups(MaterialGroupQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(MaterialGroupService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增资料分类")
    @ApiOperation("新增资料分类")
    @PreAuthorize("@el.check('admin','MaterialGroup:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody MaterialGroup resources) {
        MaterialGroupService.create(resources);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    @Log("修改资料分类")
    @ApiOperation("修改资料分类")
    @PreAuthorize("@el.check('admin','MaterialGroup:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody MaterialGroup resources) {
        MaterialGroupService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除资料分类")
    @ApiOperation("删除资料分类")
    @PreAuthorize("@el.check('admin','MaterialGroup:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Set<Integer> ids) {
        MaterialGroupService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Log("查询所有数据不分页")
    @ApiOperation("查询所有数据不分页")
    @GetMapping(value = "/queryAll")
    @PreAuthorize("@el.check('admin','MaterialGroup:list')")
    public ResponseEntity<Object> queryAll() {
        return new ResponseEntity<>(MaterialGroupService.queryAll(null), HttpStatus.OK);
    }

}