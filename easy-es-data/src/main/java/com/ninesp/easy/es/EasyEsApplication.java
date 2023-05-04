package com.ninesp.easy.es;

import org.dromara.easyes.starter.config.EsAutoConfiguration;
import org.dromara.easyes.starter.register.EsMapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ninesp
 * @date 2023/5/4
 */
@SpringBootApplication(scanBasePackages = "com.ninesp.easy.es.config",exclude = EsAutoConfiguration.class)
@EsMapperScan("com.ninesp.easy.es.document.mapper")
public class EasyEsApplication {
    public static void main(String[] args) {
        SpringApplication.run(EasyEsApplication.class, args);
    }

}
