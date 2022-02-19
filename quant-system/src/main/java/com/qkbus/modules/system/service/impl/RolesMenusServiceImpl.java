
package com.qkbus.modules.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qkbus.common.service.impl.BaseServiceImpl;
import com.qkbus.modules.system.domain.RolesMenus;
import com.qkbus.modules.system.service.RolesMenusService;
import com.qkbus.modules.system.service.mapper.RolesMenusMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author 少林一枝花
 * @date 2020-05-16
 */
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RolesMenusServiceImpl extends BaseServiceImpl<RolesMenusMapper, RolesMenus> implements RolesMenusService {

    @Override
    public void untiedMenu(Long menuId) {
        this.remove(Wrappers.<RolesMenus>lambdaUpdate().eq(RolesMenus::getMenuId, menuId));
    }
}
