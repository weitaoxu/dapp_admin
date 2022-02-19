
package com.qkbus.modules.material.rest;

import com.qkbus.dozer.service.IGenerator;
import com.qkbus.logging.aop.log.Log;
import com.qkbus.modules.material.domain.Notice;
import com.qkbus.modules.material.service.NoticeService;
import com.qkbus.modules.material.service.dto.NoticeDto;
import com.qkbus.modules.material.service.dto.NoticeQueryCriteria;
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
 * @date 2021-05-14
 */
@AllArgsConstructor
@Api(tags = "公告管理管理")
@RestController
@RequestMapping("/api/Notice")
public class NoticeController {

    private final NoticeService NoticeService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','Notice:list')")
    public void download(HttpServletResponse response, NoticeQueryCriteria criteria) throws IOException {
        NoticeService.download(generator.convert(NoticeService.queryAll(criteria), NoticeDto.class), response);
    }

    @GetMapping
    @Log("查询公告管理")
    @ApiOperation("查询公告管理")
    @PreAuthorize("@el.check('admin','Notice:list')")
    public ResponseEntity<Object> getNotices(NoticeQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(NoticeService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增公告管理")
    @ApiOperation("新增公告管理")
    @PreAuthorize("@el.check('admin','Notice:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Notice resources) {
        NoticeService.create(resources);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    @Log("修改公告管理")
    @ApiOperation("修改公告管理")
    @PreAuthorize("@el.check('admin','Notice:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Notice resources) {
        NoticeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除公告管理")
    @ApiOperation("删除公告管理")
    @PreAuthorize("@el.check('admin','Notice:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Set<Long> ids) {
        NoticeService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}