package me.project.humanstarter.configuration;

import me.project.humanstarter.data.KafkaProperties;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@ComponentScan(basePackages = "me.project.humanstarter")
@EnableConfigurationProperties({KafkaProperties.class})
public class HumanStarterConfiguration {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    public NewTopic humanStarterTopic() {
        return TopicBuilder.name(kafkaProperties.getTopic()).build();
    }


}
