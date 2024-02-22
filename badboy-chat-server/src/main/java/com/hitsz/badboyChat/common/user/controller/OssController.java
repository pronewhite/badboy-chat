package com.hitsz.badboyChat.common.user.controller;

import com.hitsz.badboyChat.common.domain.vo.resp.ApiResult;
import com.hitsz.badboyChat.common.exception.BusinessException;
import com.hitsz.badboyChat.common.user.domain.vo.resp.OssResp;
import com.hitsz.badboyChat.common.utils.OSSUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/20 21:54
 */
@RestController
@RequestMapping("/capi/user/oss")
@Api(tags = "oss相关接口")
public class OssController {

    @Autowired
    private OSSUtil ossUtil;

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public ApiResult<OssResp> getDownloadUrl(@RequestParam("file") MultipartFile file){
        try {
            return  ApiResult.success(new OssResp(ossUtil.uploadFileToOSS(file)));
        } catch (Exception e) {
            throw new BusinessException("上传文件失败");
        }
    }
}
