package com.neuedu.busines.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Component
public class FtpService {
    @Value("${ftp.host}")
    private String host;
    @Value("${ftp.user}")
    private String user;
    @Value("${ftp.password}")
    private String pass;
    //1.连接到ftp服务器
    FTPClient ftpClient = null;

    public boolean connectFTP() {
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(host); //连接服务器
            ftpClient.login(user, pass); //登录
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("================FTP连接异常================");
        }
        return false;
    }

    //2.文件上传
    public boolean uploadFile(List<File> files) {
        return uploadFile("img", files);
    }

    public boolean uploadFile(String remotePath, List<File> files) {
        FileInputStream fileInputStream = null;
        try {

            if (connectFTP()) { //先登录到ftp
                //切换工作目录
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024); //缓冲区
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);//二进制形式上传
                ftpClient.enterLocalPassiveMode(); //打开被动传输模式
                for (File f : files) {
                    fileInputStream = new FileInputStream(f);
                    ftpClient.storeFile(f.getName(), fileInputStream);
                }
                System.out.println("============fileupload success===========");
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("============fileupload error===========");
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return false;
    }
}
