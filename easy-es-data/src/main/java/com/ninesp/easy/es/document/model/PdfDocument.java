package com.ninesp.easy.es.document.model;

import lombok.Data;
import org.dromara.easyes.annotation.IndexField;
import org.dromara.easyes.annotation.rely.Analyzer;

import java.io.Serializable;

/**
 * @author chenjun
 * @since 2023/4/23
 */
@Data
public class PdfDocument implements Serializable {

    private String id;

    private String title;

    private String category;

    @IndexField(analyzer = Analyzer.IK_MAX_WORD)
    private String content;

    private String path;
}
