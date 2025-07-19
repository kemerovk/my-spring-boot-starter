package me.project.humanstarter.configuration;

import me.project.humanstarter.service.TaskService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class HumanStarterConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TaskService taskService() {
        return new TaskService();
    }



}
