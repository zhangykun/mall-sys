package com.mall.mallsys.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "file.upload")
public class FileUploadProperties {

    /** 上传文件存储路径 */
    private String path = "D:/upload/";

    /** 允许上传的文件类型 */
    private String[] allowTypes = {"jpg", "jpeg", "png", "gif", "webp", "bmp"};

    /** 文件大小限制（MB） */
    private int maxSize = 10;

    /** 访问路径前缀 */
    private String prefix = "/upload";
}
