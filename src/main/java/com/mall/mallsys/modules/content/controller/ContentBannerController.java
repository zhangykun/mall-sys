package com.mall.mallsys.modules.content.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.mallsys.common.annotation.Log;
import com.mall.mallsys.common.result.Result;
import com.mall.mallsys.modules.content.entity.ContentBanner;
import com.mall.mallsys.modules.content.service.ContentBannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/content/banner")
@Tag(name = "Banner 管理")
@PreAuthorize("hasAuthority('content:banner:list')")
public class ContentBannerController {

    private final ContentBannerService bannerService;

    public ContentBannerController(ContentBannerService bannerService) {
        this.bannerService = bannerService;
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询 Banner")
    public Result<Page<ContentBanner>> page(@RequestParam(defaultValue = "1") Integer  current,
                                            @RequestParam(defaultValue = "10") Integer size,
                                            @RequestParam(required = false) String keyword
                                            ){
        Page<ContentBanner> page = bannerService.page(
                new Page<>(current,size),
                new LambdaQueryWrapper<ContentBanner>()
                        .like(keyword!=null,ContentBanner::getTitle,keyword)
                        .orderByAsc(ContentBanner::getSort)
        );
        return Result.success(page);
    }

    /**
     * 获取 Banner 列表（不分页，用于前端展示）
     */
    @GetMapping("/list")
    @Operation(summary = "获取 Banner 列表（不分页）")
    public Result<List<ContentBanner>> list(){
        List<ContentBanner> list = bannerService.list(
                new LambdaQueryWrapper<ContentBanner>()
                        .eq(ContentBanner::getStatus, 1)
                        .orderByAsc(ContentBanner::getSort)
        );
        return Result.success(list);
    }
    /**
     * 查询 Banner 详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询 Banner 详情")
    public Result<ContentBanner> getById(@PathVariable Long id) {
        return Result.success(bannerService.getById(id));
    }
    /**
     * 新增 Banner
     */
    @PostMapping
    @Operation(summary = "新增 Banner")
    @Log("新增 Banner")
    public Result<Void> add(@Validated @RequestBody ContentBanner banner) {
        bannerService.save(banner);
        return Result.success();
    }
    /**
     * 修改 Banner
     */
    @PutMapping("/{id}")
    @Operation(summary = "修改 Banner")
    @Log("修改 Banner")
    public Result<Void> update(@PathVariable Long id, @Validated @RequestBody ContentBanner banner) {
        banner.setId(id);
        bannerService.updateById(banner);
        return Result.success();
    }

    /**
     * 删除 Banner
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除 Banner")
    @Log("删除 Banner")
    public Result<Void> delete(@PathVariable Long id) {
        bannerService.removeById(id);
        return Result.success();
    }
}
