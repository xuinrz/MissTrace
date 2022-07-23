package misstrace.Controller;

import misstrace.Entity.User;
import misstrace.Service.LoginService;
import misstrace.Service.UserService;
import misstrace.Util.JwtUtil;
import misstrace.Payload.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    UserService userService;
    @Resource
    LoginService loginService;

    @GetMapping("/hello")
    public String hello() {
        return "hello world";
    }

    @PostMapping("/login")
    public Result login( String sid, String password) {
        if (sid==null||sid.equals("")||password.equals("")||password==null){
            return Result.failure(-3,"学号或密码不能为空");
        }
//        这里暂时假设统一认证登录成功了
//       TODO  需要实现下面的CasLoginUtil.tongyirenzheng方法：接入统一认证，登录成功则返回学生姓名，失败则返回"登陆失败"
        String sduLogin = null;
        try {
            sduLogin = loginService.serve(sid,password);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        如果获取到了用户名，说明通过了统一认证，那就进入我写的User系统了（学号有记录就获取记录，学号没有被记录就添加记录）
        if(sduLogin!=null) {
            Optional<User> op = userService.findUserBySid(sid);
            String userId;
            User user;
//        如果这个用户已经被记录了,那就获取该用户（以及该用户权限）
            if (op.isPresent()) {
                user = op.get();
            } else {//没有该用户的记录，那就新建用户
                user = new User();
                user.setId(userService.getNewId());
                user.setSid(sid);
                user.setName(sduLogin);
                userService.addUser(user);
            }
            //生成JWT字符串
            userId = user.getId().toString();
            Map<String, Object> info = new HashMap<>();
            info.put("sid", sid);
            info.put("password", password);
            String token = JwtUtil.sign(userId, info);
            //传回用户信息
            Map<String, Object> data = new HashMap<>();
            data.put("isAdmin", user.getIsAdmin());//这里返回用户的权限，方便前端确定是否显示管理员入口
            data.put("avatar",user.getAvatar());
            data.put("nickName",user.getNickName());
            data.put("name",user.getName());
            return Result.success(data, token);
        }else
            return Result.failure(-2,"用户名或密码错误，统一认证登陆失败");
    }
}

