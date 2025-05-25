----------create tables in mysql----------

CREATE TABLE GATEWAY_CONFIGS (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    route_id VARCHAR(255) NOT NULL UNIQUE,
    predicates TEXT, -- Sử dụng TEXT cho chuỗi dài như JSON hoặc danh sách predicates
    filters TEXT,    -- Sử dụng TEXT cho chuỗi dài như JSON hoặc danh sách filters
    uri VARCHAR(255) NOT NULL,
    order_val INT DEFAULT 0
)

------------ run redis ----------------------
docker pull redis
docker run --name redis -p 6379:6379 -d redis