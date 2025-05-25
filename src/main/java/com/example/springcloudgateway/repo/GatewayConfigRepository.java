package com.example.springcloudgateway.repo;

import com.example.springcloudgateway.model.GatewayConfig;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GatewayConfigRepository extends ReactiveCrudRepository<GatewayConfig, Long> {
    // R2DBC Repositories trả về Mono hoặc Flux
    // Flux<GatewayConfig> findAll(); // Đã có sẵn trong ReactiveCrudRepository
}
