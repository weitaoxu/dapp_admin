
package com.qkbus.tools.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageInfo;
import com.qkbus.common.service.impl.BaseServiceImpl;
import com.qkbus.common.utils.QueryHelpPlus;
import com.qkbus.config.FileProperties;
import com.qkbus.dozer.service.IGenerator;
import com.qkbus.exception.BadRequestException;
import com.qkbus.tools.domain.LocalStorage;
import com.qkbus.tools.service.LocalStorageService;
import com.qkbus.tools.service.dto.LocalStorageDto;
import com.qkbus.tools.service.dto.LocalStorageQueryCriteria;
import com.qkbus.tools.service.mapper.LocalStorageMapper;
import com.qkbus.utils.FileUtil;
import com.qkbus.utils.SecurityUtils;
import com.qkbus.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @author 少林一枝花
 * @date 2020-05-13
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class LocalStorageServiceImpl extends BaseServiceImpl<LocalStorageMapper, LocalStorage> implements LocalStorageService {

    private final IGenerator generator;


    private final FileProperties properties;


    @Override

    public Map<String, Object> queryAll(LocalStorageQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<LocalStorage> page = new PageInfo<>(baseMapper.selectList(QueryHelpPlus.getPredicate(LocalStorage.class, criteria)));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), LocalStorageDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override

    public List<LocalStorageDto> queryAll(LocalStorageQueryCriteria criteria) {
        return generator.convert(baseMapper.selectList(QueryHelpPlus.getPredicate(LocalStorage.class, criteria)), LocalStorageDto.class);
    }

    @Override
    public LocalStorageDto findById(Long id) {
        LocalStorage localStorage = this.getById(id);
        return generator.convert(localStorage, LocalStorageDto.class);
    }

    @Override
    public LocalStorageDto create(String name, MultipartFile multipartFile) {
        FileUtil.checkSize(properties.getMaxSize(), multipartFile.getSize());
        String suffix = FileUtil.getExtensionName(multipartFile.getOriginalFilename());
        String type = FileUtil.getFileType(suffix);
        File file = FileUtil.upload(multipartFile, properties.getPath().getPath() + type + File.separator);
        if (ObjectUtil.isNull(file)) {
            throw new BadRequestException("上传失败");
        }
        try {
            name = StringUtils.isBlank(name) ? FileUtil.getFileNameNoEx(multipartFile.getOriginalFilename()) : name;
            LocalStorage localStorage = new LocalStorage(
                    file.getName(),
                    name,
                    suffix,
                    file.getPath(),
                    type,
                    FileUtil.getSize(multipartFile.getSize()),
                    SecurityUtils.getUsername()
            );
            this.save(localStorage);
            return generator.convert(localStorage, LocalStorageDto.class);
        } catch (Exception e) {
            FileUtil.del(file);
            throw e;
        }
    }


    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            LocalStorage storage = this.getById(id);
            FileUtil.del(storage.getPath());
            this.removeById(id);
        }
    }


    @Override
    public void download(List<LocalStorageDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (LocalStorageDto localStorage : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("文件真实的名称", localStorage.getRealName());
            map.put("文件名", localStorage.getName());
            map.put("后缀", localStorage.getSuffix());
//            map.put("路径", localStorage.getPath());
            map.put("类型", localStorage.getType());
            map.put("大小", localStorage.getSize());
            map.put("操作人", localStorage.getOperate());
            map.put("创建日期", localStorage.getGmtCreate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void updateLocalStorage(LocalStorageDto resources) {
        LocalStorage localStorage = this.getById(resources.getId());
        BeanUtils.copyProperties(resources, localStorage);
        this.saveOrUpdate(localStorage);
    }
}
