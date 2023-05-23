/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package com.ninesp.practice.dal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author ninesp
 * @date 2023/5/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("riding_order")
public class RidingOrderDO {

    private Integer id;

    private Integer orderId;

    private Integer userId;

    private Integer bikeId;

    private Integer bikeType;

    private LocalDateTime startTime;

    private String startLoc;

    private String endLoc;
}
