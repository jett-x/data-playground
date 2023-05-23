/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package com.ninesp.practice.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenjun
 * @since 2023/5/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RidingOrder implements Serializable {

    private Integer id;

    private Integer orderId;

    private Integer userId;

    private Integer bikeId;

    private Integer bikeType;

    private LocalDateTime startTime;

    private Location startLoc;

    private Location endLoc;
}
