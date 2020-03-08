package com.neuedu.busines.controller;

import com.neuedu.busines.utils.FtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ftp/")
public class FtpController {
    @Autowired
    FtpService ftpService;
    @RequestMapping("upload")
    public String upload() {
        List<File> files = new ArrayList<>();
        File file = new File("D:\\upload\\temp\\1.jpg");
        files.add(file);
        boolean uploadFile = ftpService.uploadFile("img", files);
        return "finish+" + uploadFile;
    }
}
