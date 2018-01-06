package de.ccs.crawler

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.PropertySource
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
@EnableSwagger2
class WebPageAnalyzerApplication

fun main(args: Array<String>) {
    SpringApplication.run(WebPageAnalyzerApplication::class.java, *args)
}
