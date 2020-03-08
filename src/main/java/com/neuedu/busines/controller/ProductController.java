package com.neuedu.busines.controller;

import com.neuedu.busines.common.Consts;
import com.neuedu.busines.common.RoleEnum;
import com.neuedu.busines.common.ServerResponse;
import com.neuedu.busines.common.StatusEnum;
import com.neuedu.busines.pojo.Product;
import com.neuedu.busines.pojo.User;
import com.neuedu.busines.service.ProductService;
import com.neuedu.busines.utils.FtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/manage/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private FtpService ftpService;
    @Value("${upload.path}")
    private String uploadPath;

    /**
     * @param file 待上传的文件 pic和表单的name一致
     * @return
     */
    @RequestMapping("/upload")
    public ServerResponse upload(@RequestParam("pic") MultipartFile file) {
        if (file == null) {
            return null;
        }
        //获取文件名称
        String filename = file.getOriginalFilename(); //文件原始名称
        if (filename == null) {
            return ServerResponse.serverResponseByFail(StatusEnum.UPLOAD_NAME_NOT_NULL.getCode(), StatusEnum.UPLOAD_NAME_NOT_NULL.getMsg());
        }
        //获取源文件拓展名
        String ext = filename.substring(filename.lastIndexOf("."));
        //生成一个新的名称
        String name = UUID.randomUUID().toString();
        String newName = ext + name;
        //创建一个文件
        File file1 = new File(uploadPath, newName);
        try {
            //将文件写入磁盘
            file.transferTo(file1);
            List<File> files = new ArrayList<>();
            files.add(file1);
            int retry = 3;
            boolean img = false;
            while (retry-->0){
                img = ftpService.uploadFile("img", files);
                if (img){
                    break;
                }
            }
            if(!img){
                return ServerResponse.serverResponseByFail(StatusEnum.PHOTO_UPLOAD_FTP_FAIL.getCode(),StatusEnum.PHOTO_UPLOAD_FTP_FAIL.getMsg());
            }
            //删除本地文件
            file1.delete();
            return ServerResponse.serverResponseBySucess(null, newName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回前端
        return null;
    }

    @RequestMapping("/save.do")
    public ServerResponse save(Product product, HttpSession session) {
        User user = (User) session.getAttribute(Consts.USER);
        if (user.getRole() != RoleEnum.ADMIN.getRole()) {
            return ServerResponse.serverResponseByFail(StatusEnum.NO_AUTHORITY.getCode(), StatusEnum.NO_AUTHORITY.getMsg());
        }
        productService.saveProduct(product);
        return ServerResponse.serverResponseBySucess();
    }

    /**
     * 按条件查询
     *
     * @param categoryId 类别ID
     * @param keyword    关键字
     * @param pageSize   第几页
     * @param pageNum    每页显示几条
     * @param orderby    按条件分组
     * @return
     */
    @RequestMapping("/list.do")
    public ServerResponse list(@RequestParam(required = false, defaultValue = "-1") Integer categoryId,
                               @RequestParam(required = false, defaultValue = "") String keyword,
                               @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                               @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                               @RequestParam(required = false, defaultValue = "") String orderby) {
        return productService.listProduct(categoryId, keyword, pageNum, pageSize, orderby);
    }

    @RequestMapping("/detail.do")
    public ServerResponse details(Integer id) {
        return productService.productDetails(id);
    }
}
