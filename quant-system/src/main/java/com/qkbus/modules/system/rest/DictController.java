
package com.qkbus.modules.system.rest;

import com.qkbus.aop.ForbidSubmit;
import com.qkbus.dozer.service.IGenerator;
import com.qkbus.logging.aop.log.Log;
import com.qkbus.modules.system.domain.Dict;
import com.qkbus.modules.system.service.DictService;
import com.qkbus.modules.system.service.dto.DictDto;
import com.qkbus.modules.system.service.dto.DictQueryCriteria;
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
import java.util.Set;

/**
 * @author 少林一枝花
 * @date 2019-04-10
 */
@Api(tags = "系统：字典管理")
@RestController
@RequestMapping("/api/dict")
public class DictController {

    private final DictService dictService;
    private final IGenerator generator;


    public DictController(DictService dictService, IGenerator generator) {
        this.dictService = dictService;
        this.generator = generator;
    }

    @Log("导出字典数据")
    @ApiOperation("导出字典数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','dict:list')")
    public void download(HttpServletResponse response, DictQueryCriteria criteria) throws IOException {
        dictService.download(generator.convert(dictService.queryAll(criteria), DictDto.class), response);
    }

    @Log("查询字典")
    @ApiOperation("查询字典")
    @GetMapping(value = "/all")
    @PreAuthorize("@el.check('admin','dict:list')")
    public ResponseEntity<Object> all() {
        return new ResponseEntity<>(dictService.queryAll(new DictQueryCriteria()), HttpStatus.OK);
    }

    @Log("查询字典")
    @ApiOperation("查询字典")
    @GetMapping
    @PreAuthorize("@el.check('admin','dict:list')")
    public ResponseEntity<Object> getDicts(DictQueryCriteria resources, Pageable pageable) {
        return new ResponseEntity<>(dictService.queryAll(resources, pageable), HttpStatus.OK);
    }

    @Log("新增字典")
    @ApiOperation("新增字典")
    @PostMapping
    @PreAuthorize("@el.check('admin','dict:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Dict resources) {
        dictService.create(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ForbidSubmit
    @Log("修改字典")
    @ApiOperation("修改字典")
    @PutMapping
    @PreAuthorize("@el.check('admin','dict:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Dict resources) {

        dictService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ForbidSubmit
    @Log("删除字典")
    @ApiOperation("删除字典")
    @DeleteMapping
    @PreAuthorize("@el.check('admin','dict:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids) {

        dictService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
