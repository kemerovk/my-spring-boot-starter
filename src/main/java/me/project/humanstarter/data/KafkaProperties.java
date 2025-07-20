package me.project.humanstarter.data;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "humanstarter.kafka")
@Data
public class KafkaProperties {
    private String bootstrapServers;
    private String topic;

}
