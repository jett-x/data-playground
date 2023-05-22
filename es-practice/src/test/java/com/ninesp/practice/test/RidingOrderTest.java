/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package com.ninesp.practice.test;

import com.ninesp.practice.dal.entity.RidingOrder;
import com.ninesp.practice.dal.mapper.RidingOrderMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @author ninesp
 * @date 2023/5/22
 */
public class RidingOrderTest extends AbstractAppTest{
    @Resource
    private RidingOrderMapper ridingOrderMapper;

    @Test
    public void testQuery() {
        RidingOrder ridingOrder = ridingOrderMapper.selectById(1L);
        Assertions.assertNotNull(ridingOrder);
    }
}
