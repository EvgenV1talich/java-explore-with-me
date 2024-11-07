package ru.yandex.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.StatsClient;

@Configuration
public class StatsClientConfig {

    @Value("${stats-server.url}")
    private String url;

    @Bean
    public StatsClient statsClient() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        return new StatsClient(url, builder);
    }
}
