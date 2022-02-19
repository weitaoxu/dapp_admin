package com.qkbus.modules.monitor.rest;


import com.qkbus.aop.ForbidSubmit;
import com.qkbus.logging.aop.log.Log;
import com.qkbus.modules.monitor.service.RedisService;
import com.qkbus.modules.monitor.vo.RedisVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author 少林一枝花
 * @date 2021-12-10
 */
@Api(tags = "redis缓存管理")
@RestController
@RequestMapping("api")
public class RedisController {

    @Autowired
    private RedisService redisService;

    @Log("查询Redis缓存")
    @GetMapping(value = "/redis")
    @PreAuthorize("hasAnyRole('ADMIN','REDIS_ALL','REDIS_SELECT')")
    public ResponseEntity getRedis(String key, Pageable pageable) {
        return new ResponseEntity(redisService.findByKey(key, pageable), HttpStatus.OK);
    }

    @ForbidSubmit
    @Log("删除Redis缓存")
    @DeleteMapping(value = "/redis")
    @PreAuthorize("hasAnyRole('ADMIN','REDIS_ALL','REDIS_DELETE')")
    public ResponseEntity delete(@RequestBody RedisVo resources) {

        redisService.delete(resources.getKey());
        return new ResponseEntity(HttpStatus.OK);
    }

    @ForbidSubmit
    @Log("清空Redis缓存")
    @DeleteMapping(value = "/redis/all")
    @PreAuthorize("hasAnyRole('ADMIN','REDIS_ALL','REDIS_DELETE')")
    public ResponseEntity deleteAll() {

        redisService.flushdb();
        return new ResponseEntity(HttpStatus.OK);
    }
}
