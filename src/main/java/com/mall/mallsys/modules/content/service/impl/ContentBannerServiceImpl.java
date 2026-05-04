package com.mall.mallsys.modules.content.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.mallsys.modules.content.entity.ContentBanner;
import com.mall.mallsys.modules.content.mapper.ContentBannerMapper;
import com.mall.mallsys.modules.content.service.ContentBannerService;
import org.springframework.stereotype.Service;

@Service
public class ContentBannerServiceImpl  extends ServiceImpl<ContentBannerMapper, ContentBanner> implements ContentBannerService {
}
