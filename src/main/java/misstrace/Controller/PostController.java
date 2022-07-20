package misstrace.Controller;

import misstrace.Entity.MatchPost;
import misstrace.Entity.MissPost;
import misstrace.Entity.User;
import misstrace.Service.MatchService;
import misstrace.Service.MissService;
import misstrace.Service.UserService;
import misstrace.Util.JwtUtil;
import misstrace.payload.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        MatchPost matchPost = new MatchPost();
        matchPost.setId(missService.getNewId());
        matchPost.setUser(user);
        matchPost.setMissPost(missPost);
        matchPost.setImg(img);
        matchPost.setPosition(position);


//      TODO 判断发帖地的距离


        matchService.addMatchPost(matchPost);
        return Result.success(newtoken);
    }

    @PostMapping("/getmiss")
    public Result PostMatch(HttpServletRequest request) {
        String token = request.getHeader("token");
        Map<String, Object> info = JwtUtil.getInfo(token);
        User user = userService.findUserBySid((String) info.get("sid")).get();

        List<MissPost> postsList = missService.findPassedMissPosts();
        List dataList = new ArrayList();
        MissPost post;
        Map m;
        for (int i = 0; i < postsList.size(); i++) {
            post = postsList.get(i);
            m =new HashMap();
            m.put("id", post.getId());
            m.put("text", post.getText());
            m.put("img", post.getImg());
            m.put("avatar", post.getUser().getAvatar());
            m.put("nickName", post.getUser().getNickName());
            m.put("sid", post.getUser().getSid());
            dataList.add(m);
        }
        //        重新生成token
        String newtoken = JwtUtil.refreshToken(token);
        return Result.success(dataList, newtoken);
    }

}


