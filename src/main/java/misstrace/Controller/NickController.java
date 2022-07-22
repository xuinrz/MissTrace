package misstrace.Controller;

import misstrace.Entity.User;
import misstrace.Service.UserService;
import misstrace.Util.ImgUtil;
import misstrace.Util.JwtUtil;
import misstrace.Payload.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/nick")
public class NickController {

    @Resource
    UserService userService;

    @PostMapping("/get")
    public Result getNickNameAndAvatar(HttpServletRequest request) {
//        读取token
        String token = request.getHeader("token");
        String userIdStr = JwtUtil.getUserId(token);
        Map<String, Object> info = JwtUtil.getInfo(token);
//        重新生成token
        String newtoken = JwtUtil.sign(userIdStr, info);
//        获取该用户并将头像昵称返回
        User user = userService.findUserBySid((String)info.get("sid")).get();
        String avatar = user.getAvatar();
        String nickName = user.getNickName();
        Map<String,Object> data = new HashMap<>();
        data.put("avatar",avatar);
        data.put("nickName",nickName);
        return Result.success(data,newtoken);
    }
    @PostMapping("/coin")
    public Result getCoin(HttpServletRequest request) {
//        读取token
        String token = request.getHeader("token");
        User user = userService.getUserByToken(token);
        Integer coin = user.getCoin();
        Map<String,Object> data = new HashMap<>();
        data.put("coin",coin);
        return Result.success(data,JwtUtil.refreshToken(token));
    }
    @PutMapping("/changenickname")
    public Result changeNickName(String nickName,HttpServletRequest request) {
        String token = request.getHeader("token");
        User user = userService.getUserByToken(token);
        user.setNickName(nickName);
        userService.modifyUser(user);
        return Result.success(JwtUtil.refreshToken(token));
    }
    @PutMapping("/changeavatar")
    public Result changeAvatar(MultipartFile avatar, HttpServletRequest request) {
        String token = request.getHeader("token");
        User user = userService.getUserByToken(token);
        String avatarPath = ImgUtil.uploadAvatar(avatar);
        user.setAvatar(avatarPath);
        userService.modifyUser(user);
        return Result.success(JwtUtil.refreshToken(token));
    }

}

