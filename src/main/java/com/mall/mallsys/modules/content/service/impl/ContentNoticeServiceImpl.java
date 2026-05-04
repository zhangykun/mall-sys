package com.mall.mallsys.modules.content.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.mallsys.modules.content.entity.ContentNotice;
import com.mall.mallsys.modules.content.mapper.ContentNoticeMapper;
import com.mall.mallsys.modules.content.service.ContentNoticeService;
import org.springframework.stereotype.Service;

@Service
public class ContentNoticeServiceImpl extends ServiceImpl<ContentNoticeMapper, ContentNotice> implements ContentNoticeService {
}
