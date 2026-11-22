package com.project.accounts.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;

@Configuration 
public class OpenTelemetryLoggingConfig {
    

    @Bean 
    ApplicationListener<ApplicationReadyEvent> openTelemetryAppenderInstaller(
        OpenTelemetry openTelemetry
    ){
        return event -> OpenTelemetryAppender.install(openTelemetry);
    }

}
