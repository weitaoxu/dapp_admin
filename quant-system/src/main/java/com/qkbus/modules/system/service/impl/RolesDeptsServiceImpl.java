
package com.qkbus.modules.system.service.impl;

import com.qkbus.common.service.impl.BaseServiceImpl;
import com.qkbus.modules.system.domain.RolesDepts;
import com.qkbus.modules.system.service.RolesDeptsService;
import com.qkbus.modules.system.service.mapper.RolesDeptsMapper;
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
public class RolesDeptsServiceImpl extends BaseServiceImpl<RolesDeptsMapper, RolesDepts> implements RolesDeptsService {

}
