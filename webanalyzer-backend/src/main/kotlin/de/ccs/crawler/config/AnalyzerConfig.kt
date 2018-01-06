package de.ccs.crawler.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer

@Configuration
@PropertySource("classpath:application.properties")
class AnalyzerConfig{

    @Bean
    fun propertyConfigIn(): PropertySourcesPlaceholderConfigurer = PropertySourcesPlaceholderConfigurer()
}