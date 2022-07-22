package misstrace.Util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ImgUtil {

    //    定义头像存储地址 /project/misstrace/tomcat8port8080/webapps
    private static final String AVATAR_PATH = "/project/misstrace/tomcat8port8080/webapps/avatar";
    //    定义访问头像的路径根部 http
    private static final String AVATAR_LOAD_PATH = "http://139.159.252.129/misstrace/avatar/";

    public static String uploadAvatar(MultipartFile img) {
        //获得上传文件的名称
        String rawFileName = img.getOriginalFilename();
        //生成随机uuid，连接到原文件名前面，防止重名
        String uuid = UUID.randomUUID().toString().replace("-","");
        String newFileName = uuid+"-"+rawFileName;
        //根据路径和新文件名，生成文件，并写入图片
        File file = new File(AVATAR_PATH,newFileName);
        try {
            img.transferTo(file);
        } catch (IOException e) {
            return null;
        }
        return AVATAR_LOAD_PATH+newFileName;
    }

    //    定义存储图片地址
    private static final String IMG_PATH = "/project/misstrace/tomcat8port8080/webapps/img";
    //    定义访问图片的路径根部
    private static final String IMG_LOAD_PATH = "http://139.159.252.129/misstrace/img/";

    public static String uploadImg(MultipartFile img) {
        //获得上传文件的名称
        String rawFileName = img.getOriginalFilename();
        //生成随机uuid，连接到原文件名前面，防止重名
        String uuid = UUID.randomUUID().toString().replace("-","");
        String newFileName = uuid+"-"+rawFileName;
        //根据路径和新文件名，生成文件，并写入图片
        File file = new File(IMG_PATH,newFileName);
        try {
            img.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return IMG_LOAD_PATH+newFileName;
    }

// TODO 删除旧头像和删除旧帖图的方法。

}
