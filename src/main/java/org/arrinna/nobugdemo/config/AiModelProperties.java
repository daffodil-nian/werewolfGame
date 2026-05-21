package org.arrinna.nobugdemo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "llm")
public class AiModelProperties {

    private String apiKey;
    private String modelName;
    private String service;
    private Double temperature;
//    private String endPoint;
}
