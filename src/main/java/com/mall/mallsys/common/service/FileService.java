package com.mall.mallsys.common.service;

import com.mall.mallsys.common.config.FileUploadProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.UUID;

@Service
public class FileService {

    private final FileUploadProperties uploadProperties;

    public FileService(FileUploadProperties uploadProperties) {
        this.uploadProperties = uploadProperties;
    }

    public FileInfo upload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        // 获取原始文件名和后缀
        String originalFilename = file.getOriginalFilename();
        String extension = getExtension(originalFilename);

        // 校验文件类型
        boolean allowed = Arrays.stream(uploadProperties.getAllowTypes())
                .anyMatch(t -> t.equalsIgnoreCase(extension));
        if (!allowed) {
            throw new IllegalArgumentException("不允许的文件类型: " + extension);
        }

        // 校验文件大小
        long maxBytes = (long) uploadProperties.getMaxSize() * 1024 * 1024;
        if (file.getSize() > maxBytes) {
            throw new IllegalArgumentException("文件大小超过限制: " + uploadProperties.getMaxSize() + "MB");
        }

        // 按日期分目录存放
        String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String relativePath = dateDir;

        // 生成新文件名（UUID + 后缀）
        String newFileName = UUID.randomUUID().toString().replace("-", "") + "." + extension;

        // 创建目录
        File dir = new File(uploadProperties.getPath(), relativePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 保存文件
        File dest = new File(dir, newFileName);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }

        // 返回访问 URL
        String url = uploadProperties.getPrefix() + "/" + relativePath + "/" + newFileName;
        return new FileInfo(originalFilename, url, file.getSize());
    }

    private String getExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }

    public record FileInfo(String originalName, String url, long size) {
    }
}
