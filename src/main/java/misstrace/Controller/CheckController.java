package misstrace.Controller;

import misstrace.Payload.Result;
import misstrace.Service.MatchService;
import misstrace.Service.MissService;
import misstrace.Service.UserService;
import misstrace.Util.JwtUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/check")
public class CheckController {

    @Resource
    UserService userService;
    @Resource
    MissService missService;
    @Resource
    MatchService matchService;

    @PostMapping("/miss")
    public Result showCheckingMissPost(HttpServletRequest request){
        String token = request.getHeader("token");
        if(userService.getUserByToken(token).getIsAdmin()) {
            List dataList = missService.showCheckingMissPost();
            String newToken = JwtUtil.refreshToken(token);
            return Result.success(dataList,newToken);
        }else return Result.failure(-3,"获取待审核迷踪帖，普通用户无权限");
    }

    @PostMapping("/passmiss/{id}")
    public Result passMissPost(@PathVariable("id") Integer missId, HttpServletRequest request){
        String token = request.getHeader("token");
        if(userService.getUserByToken(token).getIsAdmin()) {
            missService.passMissPostById(missId);
            String newToken = JwtUtil.refreshToken(token);
            return Result.success(newToken);
        }else return Result.failure(-3,"获取待审核迷踪帖，普通用户无权限");
    }
    @PostMapping("/refusemiss/{id}")
    public Result refuseMissPost(@PathVariable("id") Integer missId, HttpServletRequest request){
        String token = request.getHeader("token");
        if(userService.getUserByToken(token).getIsAdmin()) {
            missService.refuseMissPostById(missId);
            String newToken = JwtUtil.refreshToken(token);
            return Result.success(newToken);
        }else return Result.failure(-3,"获取待审核迷踪帖，普通用户无权限");
    }


    @PostMapping("/match")
    public Result showCheckingMatchPost(HttpServletRequest request){
        String token = request.getHeader("token");
        if(userService.getUserByToken(token).getIsAdmin()) {
            List dataList = matchService.showCheckingMatchPost();
            String newToken = JwtUtil.refreshToken(token);
            return Result.success(dataList,newToken);
        }else return Result.failure(-3,"获取待审核迷踪帖，普通用户无权限");
    }
    @PostMapping("/passmatch/{id}")
    public Result passMatchPost(@PathVariable("id") Integer matchId, HttpServletRequest request){
        String token = request.getHeader("token");
        if(userService.getUserByToken(token).getIsAdmin()) {
            matchService.passMatchPostById(matchId);
            String newToken = JwtUtil.refreshToken(token);
            return Result.success(newToken);
        }else return Result.failure(-3,"获取待审核迷踪帖，普通用户无权限");
    }
    @PostMapping("/refusematch/{id}")
    public Result refuseMatchPost(@PathVariable("id") Integer matchId, HttpServletRequest request){
        String token = request.getHeader("token");
        if(userService.getUserByToken(token).getIsAdmin()) {
            matchService.refuseMatchPostById(matchId);
            String newToken = JwtUtil.refreshToken(token);
            return Result.success(newToken);
        }else return Result.failure(-3,"获取待审核迷踪帖，普通用户无权限");
    }



}