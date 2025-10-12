/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import lombok.RequiredArgsConstructor;
import com.tplink.smb.common.data.management.system.domain.vo.EmailVo;
import com.tplink.smb.common.data.management.system.exception.BadRequestException;
import com.tplink.smb.common.data.management.system.modules.system.service.VerifyService;
import com.tplink.smb.common.data.management.system.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Service
@RequiredArgsConstructor
public class VerifyServiceImpl implements VerifyService {

  private final RedisUtils redisUtils;

  @Value("${code.expiration}")
  private Long expiration;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public EmailVo sendEmail(String email, String key) {
    EmailVo emailVo;
    String content;
    String redisKey = key + email;
    // 如果不存在有效的验证码，就创建一个新的
    TemplateEngine engine =
        TemplateUtil.createEngine(
            new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
    Template template = engine.getTemplate("email.ftl");
    String oldCode = redisUtils.get(redisKey, String.class);
    if (oldCode == null) {
      String code = RandomUtil.randomNumbers(6);
      // 存入缓存
      if (!redisUtils.set(redisKey, code, expiration)) {
        throw new BadRequestException("服务异常，请联系网站负责人");
      }
      content = template.render(Dict.create().set("code", code));
      // 存在就再次发送原来的验证码
    } else {
      content = template.render(Dict.create().set("code", oldCode));
    }
    emailVo = new EmailVo(Collections.singletonList(email), "ELADMIN后台管理系统", content);
    return emailVo;
  }

  @Override
  public void validated(String key, String code) {
    String value = redisUtils.get(key, String.class);
    if (!code.equals(value)) {
      throw new BadRequestException("无效验证码");
    } else {
      redisUtils.del(key);
    }
  }
}
