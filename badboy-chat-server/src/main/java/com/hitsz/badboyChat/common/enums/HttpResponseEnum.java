package com.hitsz.badboyChat.common.enums;

import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import com.google.common.base.Charsets;
import com.hitsz.badboyChat.common.domain.vo.resp.ApiResult;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/27 16:20
 */
@AllArgsConstructor
public enum HttpResponseEnum {
    ACCESS_DENIED(401, "没有访问权限，请登录");


    private Integer httpCode;
    private String desc;

    public void sendError(HttpServletResponse response) throws IOException {
        response.setStatus(httpCode);
        response.setContentType(ContentType.JSON.toString(Charsets.UTF_8));
        response.getWriter().write(JSONUtil.toJsonStr(ApiResult.fail(httpCode,desc)));
    }
}
