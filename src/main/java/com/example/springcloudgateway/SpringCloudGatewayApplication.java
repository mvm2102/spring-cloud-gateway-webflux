package com.example.springcloudgateway;

import com.example.springcloudgateway.config.ConfigLoaderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SpringCloudGatewayApplication implements CommandLineRunner {
    private final ConfigLoaderService configLoaderService;

    public SpringCloudGatewayApplication(ConfigLoaderService configLoaderService) {
        this.configLoaderService = configLoaderService;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudGatewayApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Gateway application started. Loading initial configs from database to Redis (Reactive)...");
        configLoaderService.loadAndStoreGatewayConfigsReactive()
                .subscribe(); // Đăng ký để kích hoạt reactive flow
    }
}
