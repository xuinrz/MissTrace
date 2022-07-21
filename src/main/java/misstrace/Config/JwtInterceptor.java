package misstrace.Config;

import misstrace.Payload.Result;
import misstrace.Util.JwtUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        //从 http 请求头中取出 token
        String token = request.getHeader("token");

        if (token == null) {
            response.setStatus(401);
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/json;charset=utf-8");
            outputStream.write("{\n\"code\": -1,\n\"msg\": \"无token，请重新登陆\",\n\"data\": null,\n    \"token\": \"\"\n}".getBytes(StandardCharsets.UTF_8));
            return false;
//            throw new RuntimeException("无 token ，请重新登陆");
        }

        //验证 token
        if(!JwtUtil.checkSign(token)){
            response.setStatus(401);
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/json;charset=utf-8");
            outputStream.write("{\n\"code\": -2,\n\"msg\": \"token无效或过期，请重新登录\",\n\"data\": null,\n    \"token\": \"\"\n}".getBytes(StandardCharsets.UTF_8));
            return false;
        }

        //验证通过后， 这里测试取出JWT中存放的数据
        //获取 token 中的 userId
        String userId = JwtUtil.getUserId(token);
//        System.out.println("这里还需要修改   id : " + userId);

        //获取 token 中的其他数据
        Map<String, Object> info = JwtUtil.getInfo(token);
//        info.forEach((k, v) -> System.out.println(k + ":" + v));
        return true;
    }
}

