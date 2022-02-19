
package com.qkbus.common.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.qkbus.common.service.BaseService;
import com.qkbus.common.web.param.OrderQueryParam;
import com.qkbus.common.web.param.QueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

/**
 * @author 少林一枝花
 * @since 2019-10-16
 */
@Slf4j
@SuppressWarnings("unchecked")
public abstract class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {

    protected Page<T> setPageParam(QueryParam queryParam) {
        return setPageParam(queryParam, null);
    }


    protected Page<T> setPagePageable(Pageable pageable) {
        Page<T> page = new Page<>(pageable.getPageNumber(), pageable.getPageNumber());
        page.setOrders(page.getOrders());
        return page;
    }

    protected Page<T> setPageParam(QueryParam queryParam, OrderItem defaultOrder) {
        Page<T> page = new Page<>();
        // 设置当前页码
        page.setCurrent(queryParam.getPage());
        // 设置页大小
        page.setSize(queryParam.getLimit());
        /**
         * 如果是queryParam是OrderQueryParam，并且不为空，则使用前端排序
         * 否则使用默认排序
         */
        if (queryParam instanceof OrderQueryParam) {
            OrderQueryParam orderQueryParam = (OrderQueryParam) queryParam;
            List<OrderItem> orderItems = orderQueryParam.getOrders();
            if (CollectionUtil.isEmpty(orderItems)) {
                page.setOrders(Arrays.asList(defaultOrder));
            } else {
                page.setOrders(orderItems);
            }
        } else {
            page.setOrders(Arrays.asList(defaultOrder));
        }

        return page;
    }

    protected void getPage(Pageable pageable) {
        String order = null;
        if (ObjectUtil.isNotNull(pageable.getSort())) {
            order = pageable.getSort().toString();
            order = order.replace(":", "");
            if ("UNSORTED".equals(order)) {
                order = "id desc";
            }
        }
        PageHelper.startPage(pageable.getPageNumber() + 1, pageable.getPageSize(), order);
    }

}
