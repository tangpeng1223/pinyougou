package com.pinyougou.manage.controller;

import com.pinyougou.common.util.FastDFSClient;
import com.pinyougou.vo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Date:2018/12/17
 * Author 唐鹏
 * DESC: 文件上传
 */

@RequestMapping("upload")
@RestController
public class UpLoadController {


    @PostMapping
    public Result upload(MultipartFile file) {
        try {
            //获取文件的后缀名
            String fileExtName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

            FastDFSClient fastDFSClient=new FastDFSClient("classpath:fastDfs/tracker.conf");
            //获得文件的访问路径
            String url = fastDFSClient.uploadFile(file.getBytes(), fileExtName);
            System.out.println("文件上传后的访问路径为："+url);
            return  Result.Success(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Result.fail("文件上传失败");
    }



}
