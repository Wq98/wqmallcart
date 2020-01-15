package com.wq.common;

import lombok.Data;

/**
 * @ClassName: PicUploadResult
 * @Description:
 * @author: 魏秦
 * @date: 2019/11/24  9:30
 */
@Data
public class PicUploadResult {
    /**图片上传错误不能抛出，抛出就无法进行jsp页面回调，所以设置这个标识，0表示无异常，1代表异常*/
    private Integer error=0;
    private String url;
}
