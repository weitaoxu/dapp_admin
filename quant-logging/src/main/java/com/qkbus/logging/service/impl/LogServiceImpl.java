
package com.qkbus.logging.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.qkbus.common.service.impl.BaseServiceImpl;
import com.qkbus.common.utils.QueryHelpPlus;
import com.qkbus.dozer.service.IGenerator;
import com.qkbus.logging.aop.log.Log;
import com.qkbus.logging.domain.SysLog;
import com.qkbus.logging.service.LogService;
import com.qkbus.logging.service.dto.LogErrorDTO;
import com.qkbus.logging.service.dto.LogQueryCriteria;
import com.qkbus.logging.service.dto.LogSmallDTO;
import com.qkbus.logging.service.mapper.LogMapper;
import com.qkbus.utils.FileUtil;
import com.qkbus.utils.StringUtils;
import com.qkbus.utils.ValidationUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 少林一枝花
 * @date 2021-11-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LogServiceImpl extends BaseServiceImpl<LogMapper, SysLog> implements LogService {


    private final LogMapper logMapper;

    private final IGenerator generator;

    public LogServiceImpl(LogMapper logMapper, IGenerator generator) {
        this.logMapper = logMapper;
        this.generator = generator;
    }


    @Override
    public Object queryAll(LogQueryCriteria criteria, Pageable pageable) {

        getPage(pageable);
        PageInfo<SysLog> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        String status = "ERROR";
        if (status.equals(criteria.getLogType())) {
            map.put("content", generator.convert(page.getList(), LogErrorDTO.class));
            map.put("totalElements", page.getTotal());
        }
        map.put("content", page.getList());
        map.put("totalElements", page.getTotal());
        return map;
    }

    @Override
    public List<SysLog> queryAll(LogQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(SysLog.class, criteria));
    }

    @Override
    public Object queryAllByUser(LogQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<SysLog> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), LogSmallDTO.class));
        map.put("totalElements", page.getTotal());
        return map;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(String username, String browser, String ip, ProceedingJoinPoint joinPoint,
                     SysLog sysLog, Long uid) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log aopLog = method.getAnnotation(Log.class);

        // 方法路径
        String methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()";

        StringBuilder params = new StringBuilder("{");
        //参数值
        Object[] argValues = joinPoint.getArgs();
        //参数名称
        String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        if (argValues != null) {
            for (int i = 0; i < argValues.length; i++) {
                params.append(" ").append(argNames[i]).append(": ").append(argValues[i]);
            }
        }
        assert sysLog != null;
        // 描述
        sysLog.setDescription(aopLog.value());
        //类型 0-后台 1-前台
        sysLog.setType(aopLog.type());
        if (uid != null) {
            sysLog.setUid(uid);
        }
        sysLog.setRequestIp(ip);
        String loginPath = "login";
        if (loginPath.equals(signature.getName())) {
            try {
                assert argValues != null;
                username = new JSONObject(argValues[0]).get("username").toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sysLog.setAddress(StringUtils.getCityInfo(sysLog.getRequestIp()));
        sysLog.setMethod(methodName);
        sysLog.setUsername(username);
        sysLog.setBrowser(browser);
        sysLog.setParams(params + " }");
        this.save(sysLog);
    }

    @Override
    public Object findByErrDetail(Long id) {
        SysLog sysLog = this.getById(id);
        ValidationUtil.isNull(sysLog.getId(), "Log", "id", id);
        byte[] details = sysLog.getExceptionDetail();
        return Dict.create().set("exception", new String(ObjectUtil.isNotNull(details) ? details : "".getBytes()));
    }

    @Override
    public void download(List<SysLog> sysLogs, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SysLog sysLog : sysLogs) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", sysLog.getUsername());
            map.put("IP", sysLog.getRequestIp());
            map.put("IP来源", sysLog.getAddress());
            map.put("描述", sysLog.getDescription());
            map.put("浏览器", sysLog.getBrowser());
            map.put("请求耗时/毫秒", sysLog.getTime());
            map.put("异常详情", new String(ObjectUtil.isNotNull(sysLog.getExceptionDetail()) ? sysLog.getExceptionDetail() : "".getBytes()));
            map.put("创建日期", sysLog.getGmtCreate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delAllByError() {
        this.remove(Wrappers.<SysLog>lambdaQuery().eq(SysLog::getLogType, "ERROR"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delAllByInfo() {
        this.remove(Wrappers.<SysLog>lambdaQuery().eq(SysLog::getLogType, "INFO"));
    }
}
