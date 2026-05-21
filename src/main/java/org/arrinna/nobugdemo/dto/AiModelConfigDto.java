package org.arrinna.nobugdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 这是AI模型配置，里面包括以下内容
 * 1.模型ID
 * 2.模型Name
 * 3.模型APIKey
 * 4.模型Service
 * 5.模型ModelName
 * 6.有时会有temperature温度，温度越高大模型越发散
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiModelConfigDto {
    private String id;              // openai-gpt4
    private String name;            // GPT-4
    private String service;         // openai / deepseek / zhipuai / dashscope
    private String apiKey;
    private String modelName;       // gpt-4 / deepseek-chat 等
    private Double temperature;
}
