
package com.qkbus.modules.material.rest;

import com.qkbus.dozer.service.IGenerator;
import com.qkbus.logging.aop.log.Log;
import com.qkbus.modules.material.domain.Material;
import com.qkbus.modules.material.service.MaterialService;
import com.qkbus.modules.material.service.dto.MaterialDto;
import com.qkbus.modules.material.service.dto.MaterialQueryCriteria;
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
@Api(tags = "资料管理管理")
@RestController
@RequestMapping("/api/Material")
public class MaterialController {

    private final MaterialService MaterialService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','Material:list')")
    public void download(HttpServletResponse response, MaterialQueryCriteria criteria) throws IOException {
        MaterialService.download(generator.convert(MaterialService.queryAll(criteria), MaterialDto.class), response);
    }

    @GetMapping
    @Log("查询资料管理")
    @ApiOperation("查询资料管理")
    @PreAuthorize("@el.check('admin','Material:list')")
    public ResponseEntity<Object> getMaterials(MaterialQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(MaterialService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增资料管理")
    @ApiOperation("新增资料管理")
    @PreAuthorize("@el.check('admin','Material:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Material resources) {
        MaterialService.create(resources);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    @Log("修改资料管理")
    @ApiOperation("修改资料管理")
    @PreAuthorize("@el.check('admin','Material:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Material resources) {
        MaterialService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除资料管理")
    @ApiOperation("删除资料管理")
    @PreAuthorize("@el.check('admin','Material:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Set<Integer> ids) {
        MaterialService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}