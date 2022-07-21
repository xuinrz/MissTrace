package misstrace.Controller;

import misstrace.Entity.MatchPost;
import misstrace.Entity.MissPost;
import misstrace.Entity.User;
import misstrace.Service.MatchService;
import misstrace.Service.MissService;
import misstrace.Service.UserService;
import misstrace.Util.JwtUtil;
import misstrace.Payload.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/post")
public class PostController {

    @Resource
    UserService userService;
    @Resource
    MissService missService;
    @Resource
    MatchService matchService;


    @PostMapping("/miss")
    public Result PostMiss(String text, String img, String position, HttpServletRequest request) {
        if (img == null) return Result.failure(-1, "图片不能为空");
//        读取token
        String token = request.getHeader("token");
        String userIdStr = JwtUtil.getUserId(token);
        Map<String, Object> info = JwtUtil.getInfo(token);
//        重新生成token
        String newtoken = JwtUtil.sign(userIdStr, info);

        User user = userService.findUserBySid((String) info.get("sid")).get();
        MissPost missPost = new MissPost();
        missPost.setId(missService.getNewId());
        missPost.setUser(user);
        missPost.setText(text);
        missPost.setImg(img);
        missPost.setPosition(position);
//        设定发帖时间
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
        String nowTime = dateFormat.format(date);
        System.out.println(nowTime);
        missPost.setPostTime(nowTime);

        missService.addMissPost(missPost);
        return Result.success(newtoken);
    }

    @PostMapping("/match")
    public Result PostMatch(Integer missId, String img, String position, HttpServletRequest request) {
        if (img == null) return Result.failure(-1, "图片不能为空");
//        读取token
        String token = request.getHeader("token");
//        String userIdStr = JwtUtil.getUserId(token);
        Map<String, Object> info = JwtUtil.getInfo(token);
//        重新生成token
        String newtoken = JwtUtil.refreshToken(token);

        User user = userService.findUserBySid((String) info.get("sid")).get();
        MissPost missPost = missService.findMissPostById(missId).get();
        if(missPost.getIsMatched()){
            return Result.failure(-1,"该迷踪帖已被匹配！");
        }
        if(missPost.getIsMatching()){
            return Result.failure(-1,"无法匹配。已经有人在匹配此帖了，看看其他迷踪帖吧");
        }


        MatchPost matchPost = new MatchPost();
        matchPost.setId(missService.getNewId());
        matchPost.setUser(user);
        matchPost.setMissPost(missPost);
        matchPost.setImg(img);
//        设定发帖时间
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
        String nowTime = dateFormat.format(date);
        System.out.println(nowTime);
        matchPost.setPostTime(nowTime);
        matchPost.setPosition(position);


//      TODO 判断发帖地的距离


//      这里把迷踪帖设为“有待审匹配帖”，从而不被搜索到
        missPost.setIsMatching(true);
        missService.updateMissPost(missPost);
        matchService.addMatchPost(matchPost);
        return Result.success(newtoken);
    }

    @PostMapping("/showmiss")
    public Result showMiss(HttpServletRequest request) {
        String token = request.getHeader("token");
        Map<String, Object> info = JwtUtil.getInfo(token);
        User user = userService.findUserBySid((String) info.get("sid")).get();

        List dataList = missService.showPosts();
        //        重新生成token
        String newtoken = JwtUtil.refreshToken(token);
        return Result.success(dataList, newtoken);
    }

}


