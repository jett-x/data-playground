/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package com.ninesp.practice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ninesp
 * @date 2023/5/22
 */
@SpringBootApplication(scanBasePackages = "com.ninesp.practice.config")
public class EsPracticeApp {
    public static void main(String[] args) {
        SpringApplication.run(EsPracticeApp.class, args);
    }
}
