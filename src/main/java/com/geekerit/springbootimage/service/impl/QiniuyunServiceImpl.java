package com.geekerit.springbootimage.service.impl;//package com.chengnanhuakai.upload.service.impl;

import com.geekerit.springbootimage.constants.QiNiuYunConstants;
import com.geekerit.springbootimage.service.QiniuyunService;
import com.geekerit.springbootimage.utils.QiNiuYunUtil;
import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName QiniuyunServiceImpl
 * @Description 七牛云服务
 * @Author Aaryn
 * @Date 2018/11/7 11:33
 * @Version 1.0
 */
@Service
public class QiniuyunServiceImpl implements QiniuyunService {

    private static final Logger logger = LoggerFactory.getLogger(QiniuyunServiceImpl.class);

    /** 获取auth */
    private final Auth auth = QiNiuYunUtil.getAuth();

    @Override
    public String getSimpleToken() {
        String upToken = auth.uploadToken(QiNiuYunConstants.BUCKET);
        logger.info("Create simple upload token : " + upToken);
        return upToken;
    }

    @Override
    public String getOverrideToken(String fileKey) {
        String upToken = auth.uploadToken(QiNiuYunConstants.BUCKET, fileKey);
        logger.info("Create override upload token : " + upToken);
        return upToken;
    }

    @Override
    public String getWithReturnToken() {
        StringMap putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
        long expireSeconds = 3600;
        String upToken = auth.uploadToken(QiNiuYunConstants.BUCKET, null, expireSeconds, putPolicy);
        logger.info("Create upload token with return info : " + upToken);
        return upToken;
    }

    @Override
    public String uploadImageToQiniuyun(MultipartFile multipartFile) throws Exception{
        String uploadToken = this.getWithReturnToken();
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        UploadManager uploadManager = new UploadManager(cfg);
        // 获取文件名称
        String originalFilename = multipartFile.getOriginalFilename();
        // 生成UUID作为文件的KEY值防止出现上传文件时文件名重复
        String uuid = UUID.randomUUID().toString();
        InputStream inputStream = multipartFile.getInputStream();
        // 默认不指定key的情况下，以文件内容的hash值作为文件名
        // 使用（文件前缀+UUID+文件名称）作为文件的空间key，防止出现key重复
        String key = "image/upload/" + uuid + "/" + originalFilename;
        Map<String,String> imageMap = new HashMap(16);
        Response response = uploadManager.put(inputStream, key, uploadToken,null,null);
        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        logger.info("解析结果为--" + putRet.toString());


        String imageUrl = QiNiuYunConstants.IMAGE_URL_HTTP_VALUE + key + QiNiuYunConstants.IMAGE_URL_HTTP_VALUE_SUFFIX;
        return imageUrl;
    }


}
