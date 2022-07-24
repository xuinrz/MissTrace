package misstrace.Util;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ImgUtil {

    //    定义头像存储地址 /project/misstrace/tomcat8port8080/webapps
    private static final String AVATAR_PATH = "/project/misstrace/tomcat8port8080/webapps/avatar/";
    //    定义访问头像的路径根部 http
    private static final String AVATAR_LOAD_PATH = "http://139.159.252.129/misstrace/avatar/";

    public static String uploadAvatar(MultipartFile avatar) {
        //获得上传文件的名称
        String rawFileName = avatar.getOriginalFilename();
        //生成随机uuid，连接到原文件名前面，防止重名
        String uuid = UUID.randomUUID().toString().replace("-","");
        String newFileName = uuid+"-"+rawFileName;
        //根据路径和新文件名，生成文件，并写入图片
        File file = new File(AVATAR_PATH,newFileName);
        //压缩图片
        Boolean flag = zipAvatar(avatar,AVATAR_PATH+newFileName);
        if(!flag)return null;
        return AVATAR_LOAD_PATH+newFileName;
    }

    //    定义存储图片地址
    private static final String IMG_PATH = "/project/misstrace/tomcat8port8080/webapps/img/";
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
        //压缩图片
        Boolean flag = zipImg(img,IMG_PATH+newFileName);
        if(!flag)return null;
        return IMG_LOAD_PATH+newFileName;

    }


public static Boolean deleteImg(String imgLoadPath) {
    boolean flag = false;
    String filePath = "/project/misstrace/tomcat8port8080/webapps/"+imgLoadPath.substring(33);
    //根据路径创建文件对象
    File file = new File(filePath);
    //路径是个文件且不为空时删除文件
    if(file.isFile()&&file.exists()){
        flag = file.delete();
    }
    //删除失败时，返回false
    return flag;

}

public static Boolean zipImg(MultipartFile img,String path) {
    try {
        //先压缩并保存图片
        Thumbnails.of(img.getInputStream()).size(720,1080)  //压缩尺寸 范围（0.00--1.00）
                .outputQuality(0.4f)  //压缩质量 范围（0.00--1.00）
                .toFile(path); //输出路径
    } catch (IOException e) {
        e.printStackTrace();
        return false;
    }
        return true;
}
    public static Boolean zipAvatar(MultipartFile img,String path) {
        try {
            //先压缩并保存图片
            Thumbnails.of(img.getInputStream()).size(128,128)  //压缩尺寸 范围（0.00--1.00）
                    .outputQuality(0.4f)  //压缩质量 范围（0.00--1.00）
                    .toFile(path); //输出路径
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
