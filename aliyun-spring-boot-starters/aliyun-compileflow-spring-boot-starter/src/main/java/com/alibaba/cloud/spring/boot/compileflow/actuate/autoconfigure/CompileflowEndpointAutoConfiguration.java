package com.alibaba.cloud.spring.boot.compileflow.actuate.autoconfigure;

import com.alibaba.cloud.spring.boot.compileflow.actuate.CompileflowEndpoint;
import com.alibaba.cloud.spring.boot.compileflow.env.CompileflowProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yusu
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CompileflowProperties.class)
@ConditionalOnClass(Endpoint.class)
public class CompileflowEndpointAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnAvailableEndpoint
    public CompileflowEndpoint redisDescribeAvailableResourceEndpoint() {
        return new CompileflowEndpoint();
    }

}
