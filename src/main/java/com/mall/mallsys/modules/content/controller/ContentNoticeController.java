package com.mall.mallsys.modules.content.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.mallsys.common.annotation.Log;
import com.mall.mallsys.common.result.Result;
import com.mall.mallsys.modules.content.entity.ContentNotice;
import com.mall.mallsys.modules.content.service.ContentNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/content/notice")
@Tag(name = "公告管理")
@PreAuthorize("hasAuthority('content:notice:list')")
public class ContentNoticeController {

    private final ContentNoticeService noticeService;

    public ContentNoticeController(ContentNoticeService noticeService) {
        this.noticeService = noticeService;
    }

    /**
     * 分页查询公告（支持关键词 + 状态筛选）
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询公告")
    public Result<Page<ContentNotice>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        Page<ContentNotice> page = noticeService.page(
                new Page<>(current, size),
                new LambdaQueryWrapper<ContentNotice>()
                        .like(keyword != null, ContentNotice::getTitle, keyword)
                        .eq(status != null, ContentNotice::getStatus, status)
                        .orderByDesc(ContentNotice::getIsTop)
                        .orderByDesc(ContentNotice::getCreateTime)
        );
        return Result.success(page);
    }

    /**
     * 查询公告详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询公告详情")
    public Result<ContentNotice> getById(@PathVariable Long id) {
        return Result.success(noticeService.getById(id));
    }

    /**
     * 新增公告
     */
    @PostMapping
    @Operation(summary = "新增公告")
    @Log("新增公告")
    public Result<Void> add(@Validated @RequestBody ContentNotice notice) {
        noticeService.save(notice);
        return Result.success();
    }

    /**
     * 修改公告
     */
    @PutMapping("/{id}")
    @Operation(summary = "修改公告")
    @Log("修改公告")
    public Result<Void> update(@PathVariable Long id, @Validated @RequestBody ContentNotice notice) {
        notice.setId(id);
        noticeService.updateById(notice);
        return Result.success();
    }

    /**
     * 删除公告
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除公告")
    @Log("删除公告")
    public Result<Void> delete(@PathVariable Long id) {
        noticeService.removeById(id);
        return Result.success();
    }
}