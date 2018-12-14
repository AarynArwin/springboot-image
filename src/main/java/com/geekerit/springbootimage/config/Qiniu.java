package com.geekerit.springbootimage.config;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @ClassName Qiniu
 * @Description 七牛云配置信息（个人中心--秘钥管理）
 * @Author Aaryn
 * @Date 2018/9/20 10:22
 * @Version 1.0
 */
@Component
@ConfigurationProperties(prefix = "qiniu")
@Data
public class Qiniu {

    /** 七牛云秘钥AK */
    private String accessKey;
    /** 七牛云秘钥SK */
    private String secretKey;
    /** 七牛云空间名称 */
    private String bucket;

}
