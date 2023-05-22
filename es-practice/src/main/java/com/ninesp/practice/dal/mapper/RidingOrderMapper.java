/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package com.ninesp.practice.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ninesp.practice.dal.entity.RidingOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ninesp
 * @date 2023/5/22
 */
@Mapper
public interface RidingOrderMapper extends BaseMapper<RidingOrder> {
}
