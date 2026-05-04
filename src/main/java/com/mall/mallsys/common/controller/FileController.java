package com.mall.mallsys.common.controller;

import com.mall.mallsys.common.result.Result;
import com.mall.mallsys.common.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/file")
@Tag(name = "文件上传")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    public Result<FileService.FileInfo> upload(@RequestParam("file") MultipartFile file) {
        FileService.FileInfo fileInfo = fileService.upload(file);
        log.info("文件上传成功: {} -> {}", fileInfo.originalName(), fileInfo.url());
        return Result.success(fileInfo);
    }
}
