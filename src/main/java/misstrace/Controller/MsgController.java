package misstrace.Controller;

import misstrace.Entity.User;
import misstrace.Payload.Result;
import misstrace.Service.UserService;
import misstrace.Util.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/msg")
public class MsgController {

    @Resource
    UserService userService;

@PostMapping("")
public Result showMyProperty(HttpServletRequest request){
    String token = request.getHeader("token");
    User user = userService.getUserByToken(token);
    List dataList = userService.getMsgByUserId(user.getId());
    return Result.success(dataList, JwtUtil.refreshToken(token));
}





}
