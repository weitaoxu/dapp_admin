
package com.qkbus.modules.system.service;

import com.qkbus.common.service.BaseService;
import com.qkbus.modules.system.domain.RolesMenus;

/**
 * @author 少林一枝花
 * @date 2020-05-16
 */
public interface RolesMenusService extends BaseService<RolesMenus> {


    void untiedMenu(Long menuId);

}
