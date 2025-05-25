package com.example.springcloudgateway.config;

import com.example.springcloudgateway.repo.GatewayConfigRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ConfigLoaderService {
    private final GatewayConfigRepository gatewayConfigRepository;
    private final ReactiveStringRedisTemplate reactiveRedisTemplate; // Thay đổi này!
    private final ObjectMapper objectMapper;

    public ConfigLoaderService(GatewayConfigRepository gatewayConfigRepository, ReactiveStringRedisTemplate reactiveRedisTemplate, ObjectMapper objectMapper) {
        this.gatewayConfigRepository = gatewayConfigRepository;
        this.reactiveRedisTemplate = reactiveRedisTemplate;
        this.objectMapper = objectMapper;
    }

    public Mono<Void> loadAndStoreGatewayConfigsReactive() { // Trả về Mono<Void> cho reactive flow
        System.out.println("Loading gateway configs from Oracle (Reactive)...");
        return gatewayConfigRepository.findAll() // Trả về Flux<GatewayConfig>
                .collectList() // Gom tất cả vào một List trong Mono
                .doOnNext(configs -> log.info("Found " + configs.size() + " gateway configs from Oracle."))
                .flatMapMany(Flux::fromIterable) // Chuyển lại thành Flux để xử lý từng config
                .flatMap(config -> {
                    try {
                        String key = "gateway_routes:" + config.getRouteId();
                        String value = objectMapper.writeValueAsString(config);
                        return reactiveRedisTemplate.opsForValue().set(key, value) // reactive set
                                .doOnSuccess(success -> System.out.println("Stored config for routeId: " + config.getRouteId() + " in Redis."))
                                .onErrorResume(e -> {
                                    log.info("Error storing config for routeId " + config.getRouteId() + " in Redis: " + e.getMessage());
                                    return Mono.empty(); // Tiếp tục xử lý các config khác
                                });
                    } catch (JsonProcessingException e) {
                        log.info("Error converting GatewayConfig to JSON for routeId " + config.getRouteId() + ": " + e.getMessage());
                        return Mono.empty(); // Bỏ qua config này
                    }
                })
                .then() // Chuyển đổi Flux<Void> thành Mono<Void> khi hoàn thành
                .doOnSuccess(v -> log.info("All gateway configs loaded to Redis (Reactive)."))
                .doOnError(e -> log.info("Error during reactive config loading: " + e.getMessage()));
    }
}
