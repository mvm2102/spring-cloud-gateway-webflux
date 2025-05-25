package com.example.springcloudgateway.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "GATEWAY_CONFIGS")
public class GatewayConfig {
    @Id
    private Long id;

    @Column("ROUTE_ID")
    private String routeId;

    @Column("PREDICATES")
    private String predicates; // JSON string hoặc chuỗi các Predicates, ví dụ: "- Path=/api/**"

    @Column("FILTERS")
    private String filters;    // JSON string hoặc chuỗi các Filters, ví dụ: "- RewritePath=/api/(?<segment>.*), /${segment}"

    @Column("URI")
    private String uri;        // URI của dịch vụ đích, ví dụ: "lb://YOUR-SERVICE-NAME" hoặc "http://localhost:8081"

    @Column("ORDER_VAL") // Đặt tên khác nếu "ORDER" là từ khóa reserved của Oracle
    private Integer orderVal;
}
