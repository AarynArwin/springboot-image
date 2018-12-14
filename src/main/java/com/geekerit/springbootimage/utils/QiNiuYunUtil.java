package com.geekerit.springbootimage.utils;//package com.chengnanhuakai.upload.utils;

import com.geekerit.springbootimage.config.Qiniu;
import com.geekerit.springbootimage.constants.QiNiuYunConstants;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @ClassName QiNiuYunUtil
 * @Description TODO
 * @Author Aaryn
 * @Date 2018/10/9 14:18
 * @Version 1.0
 */
@Component
public class QiNiuYunUtil {

    /**
     * 创建七牛云上传凭证所需auth
     * @return
     */
    public static Auth getAuth() {
        String accessKey = QiNiuYunConstants.AK;
        String secretKey = QiNiuYunConstants.SK;
        String bucket = QiNiuYunConstants.BUCKET;
        // Parameter Calibration
        if (StringUtils.isEmpty(accessKey) || StringUtils.isEmpty(secretKey) || StringUtils.isEmpty(bucket)){
            throw new QiniuException("Lack Of Key Parameters!");
        }
        return Auth.create(accessKey, secretKey);
    }

}
