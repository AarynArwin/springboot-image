package com.geekerit.springbootimage.controller;//package com.chengnanhuakai.upload.controller;

import com.geekerit.springbootimage.service.QiniuyunService;
import com.geekerit.springbootimage.vo.ResultVO;
import com.qiniu.common.QiniuException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName ImageUploadController
 * @Description 七牛云图片上传整理
 * @Author Aaryn
 * @Date 2018/9/20 9:34
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/image")
@Api(description = "七牛云图片上传接口")
public class ImageUploadController {

    private static final Logger logger = LoggerFactory.getLogger(ImageUploadController.class);

    @Autowired
    private QiniuyunService qiniuyunService;

    /**
     * 生成七牛云简单上传凭证
     * @return
     */
    @RequestMapping(value = "/simple",method = RequestMethod.GET)
    @ApiOperation(value = "生成七牛云上传简单凭证")
    public String uploadTokenSimple(){
        String simpleToken = qiniuyunService.getSimpleToken();
        return simpleToken;
    }


    /**
     * 生成七牛云覆盖上传凭证
     * @param fileKey   需要覆盖的文件名称
     * @return          覆盖上传凭证
     */
    @RequestMapping(value = "/override",method = RequestMethod.GET)
    public String uploadTokenOverride(String fileKey){
        String overrideToken = qiniuyunService.getOverrideToken(fileKey);
        return overrideToken;
    }

    /**
     * 带有回调信息的上传凭证
     * @return  上传凭证
     */
    public String uploadTokenWithReturn(){
        String withReturnToken = qiniuyunService.getWithReturnToken();
        return withReturnToken;
    }

    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    @ApiOperation(value = "上传本地文件到七牛云中，待补充从本地获取文件路径以及将上传凭证保存至本地")
    public String uploadImage(@RequestParam(value = "file") MultipartFile file, Model model) throws Exception {
        ResultVO resultVO = new ResultVO();
        try {
            String s = qiniuyunService.uploadImageToQiniuyun(file);
            resultVO.setData(s);
            model.addAttribute("data", resultVO);
        } catch (QiniuException e) {
            logger.error("七牛云上传文件异常" + e.getMessage());
            resultVO.setErrorMsg("七牛云上传文件异常");
            resultVO.setSuccess(false);
        } catch (Exception e) {
            logger.error("七牛云上传文件异常" + e.getMessage());
            resultVO.setErrorMsg("七牛云上传文件异常");
            resultVO.setSuccess(false);
        }
        return "success";
    }

    @RequestMapping(value = "/callback",method = RequestMethod.GET)
    public String getCallBackBody(HttpServletRequest request){
        String returnBody = request.getParameter("returnBody");

        if (returnBody != null){
            System.out.println("七牛云返回信息为" + returnBody);
        }

        return returnBody;
    }

}
