package misstrace.Controller;

import misstrace.Entity.User;
import misstrace.Service.UserService;
import misstrace.Util.JwtUtil;
import misstrace.Payload.Result;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/changenickname")
    public Result changeNickName(String nickName,HttpServletRequest request) {
//        读取token
        String token = request.getHeader("token");
//        String userIdStr = JwtUtil.getUserId(token);
        Map<String, Object> info = JwtUtil.getInfo(token);
//        重新生成token
        String newtoken = JwtUtil.refreshToken(token);
//        获取该用户并将头像昵称返回
        User user = userService.findUserBySid((String)info.get("sid")).get();
        user.setNickName(nickName);
        userService.modifyUser(user);
        return Result.success(newtoken);
    }
    @PostMapping("/changeavatar")
    public Result changeAvatar(String avatar,HttpServletRequest request) {
//        读取token
        String token = request.getHeader("token");
//        String userIdStr = JwtUtil.getUserId(token);
        Map<String, Object> info = JwtUtil.getInfo(token);
//        重新生成token
        String newtoken = JwtUtil.refreshToken(token);
//        获取该用户并将头像昵称返回
        User user = userService.findUserBySid((String)info.get("sid")).get();
        user.setAvatar(avatar);
        userService.modifyUser(user);
        return Result.success(newtoken);
    }

}

