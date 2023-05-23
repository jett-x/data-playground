/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package com.ninesp.practice.test;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ninesp.practice.dal.entity.RidingOrderDO;
import com.ninesp.practice.dal.mapper.RidingOrderMapper;
import com.ninesp.practice.domain.Location;
import com.ninesp.practice.domain.RidingOrder;
import com.ninesp.practice.util.GeoHashConverter;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author chenjun
 * @since 2023/5/23
 */
public class ElasticTest extends AbstractAppTest{
    @Resource
    private RestHighLevelClient client;
    @Resource
    private RidingOrderMapper ridingOrderMapper;
    @Resource
    private GeoHashConverter geoHashConverter;
    @Test
    public void testRawCreateIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("riding_order");
        request.mapping("{\n"
            + "    \"properties\": {\n"
            + "      \"userId\":{\n"
            + "        \"type\": \"integer\"\n"
            + "      },\n"
            + "      \"orderId\":{\n"
            + "        \"type\": \"integer\"\n"
            + "      },\n"
            + "      \"bikeId\":{\n"
            + "        \"type\": \"integer\"\n"
            + "      },\n"
            + "      \"bikeType\":{\n"
            + "        \"type\": \"integer\"\n"
            + "      },\n"
            + "      \"startTime\":{\n"
            + "        \"type\": \"date\"\n"
            + "      },\n"
            + "      \"startLoc\":{\n"
            + "        \"type\": \"geo_point\"\n"
            + "      },\n"
            + "      \"endLoc\":{\n"
            + "        \"type\": \"geo_point\"\n"
            + "      }\n"
            + "    }\n"
            + "  }", XContentType.JSON);
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    @Test
    public void testRawBulk() throws IOException {
        boolean hasNext = true;
        Integer start = 63780;
        while (hasNext) {
            List<RidingOrderDO> orders = ridingOrderMapper.selectList(
                new QueryWrapper<RidingOrderDO>().gt("id", start).last("limit 1000"));
            if (CollectionUtils.isEmpty(orders)) {
                hasNext = false;
                break;
            }
            BulkRequest bulkRequest = new BulkRequest();
            for (RidingOrderDO order : orders) {
                RidingOrder ridingOrder = BeanUtil.copyProperties(order, RidingOrder.class, "startLoc", "endLoc");
                double[] s = geoHashConverter.decode(order.getStartLoc());
                double[] e = geoHashConverter.decode(order.getEndLoc());
                ridingOrder.setStartLoc(Location.builder()
                        .lat(s[0]).lon(s[1])
                    .build());
                ridingOrder.setEndLoc(Location.builder()
                        .lat(e[0]).lon(e[1])
                    .build());

                bulkRequest.add(
                    new IndexRequest("riding_order").source(JSON.parseObject(JSON.toJSONString(ridingOrder))));
                start = order.getId();
            }
            BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            System.out.println(response.status().name());
        }
    }

    @Test
    public void testRawQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest("riding_order");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.rangeQuery("userId").gt(12000));
        searchSourceBuilder.sort(new FieldSortBuilder("startTime").order(SortOrder.DESC));
        searchRequest.source(searchSourceBuilder);

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsString());
        }
    }

    @Test
    public void testGeoSearch() throws IOException {
        this.searchWithinDistance("riding_order", 39.96963, 116.34933, 10, DistanceUnit.KILOMETERS);
    }

    private void searchWithinDistance(String index, double lat, double lon, double distance, DistanceUnit unit)
        throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);

        // 构建查询
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.geoDistanceQuery("startLoc").point(lat, lon).distance(distance, unit));
        sourceBuilder.query(boolQueryBuilder);

        // 设置返回结果包含哪些字段
        String[] includes = new String[] {"userId", "orderId", "endLoc"};
        String[] excludes = new String[] {"startLoc", "bikeId"};
        FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);
        sourceBuilder.fetchSource(fetchSourceContext);

        searchRequest.source(sourceBuilder);

        // 执行搜索
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        // 处理搜索结果
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            System.out.println(hit.getSourceAsString());
        }
    }
}
