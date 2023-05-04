package com.ninesp.easy.es.test;

import com.ninesp.easy.es.document.mapper.DocumentMapper;
import com.ninesp.easy.es.document.model.PdfDocument;
import org.dromara.easyes.core.core.EsWrappers;
import org.elasticsearch.action.search.SearchResponse;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @author ninesp
 * @date 2023/5/4
 */
public class EasyEsTest extends AbstractAppTest{
    @Resource
    private DocumentMapper documentMapper;

    @Test
    public void testQuery() {

        SearchResponse response =
            documentMapper.search(EsWrappers.lambdaQuery(PdfDocument.class).eq("category", "test1234"));
        System.out.printf("" + response);
    }
}
