package misstrace.Controller;

import misstrace.Entity.User;
import misstrace.Service.UserService;
import misstrace.Util.JwtUtil;
import misstrace.Payload.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    UserService userService;

    @GetMapping("/hello")
    public String hello() {
        return "hello world";
    }

    @PostMapping("/login")
    public Result login( String sid, String password) {
        if (sid==null||sid.equals("")||password.equals("")||password==null){
            return Result.failure(-1,"学号或密码不能为空");
        }


        String cookie = userService.casLogin(sid,password);
//        这里暂时假设统一认证登录成功了！






        Boolean isAdmin = false;
        Optional<User> op = userService.findUserBySid(sid);
        String userId = null;
//        如果这个用户已经被记录了,那就返回一个token，就是说登陆成功了
        if(op.isPresent()){
             userId = op.get().getId().toString();
             isAdmin = op.get().getIsAdmin();
        }
        else{
            User user = new User();
            user.setId(userService.getNewId());
            user.setSid(sid);
            userService.addUser(user);
            userId = user.getId().toString();
        }
        Map<String, Object> info = new HashMap<>();
        info.put("sid", sid);
        info.put("password", password);
        //生成JWT字符串
        String token = JwtUtil.sign(userId, info);
        Map<String,Object> data = new HashMap<>();
        data.put("isAdmin",isAdmin);
        return Result.success(data,token);

    }
}

