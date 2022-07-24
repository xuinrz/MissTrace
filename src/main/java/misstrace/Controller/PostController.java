package misstrace.Controller;

import misstrace.Entity.MatchPost;
import misstrace.Entity.MissPost;
import misstrace.Entity.User;
import misstrace.Service.MatchService;
import misstrace.Service.MissService;
import misstrace.Service.UserService;
import misstrace.Util.DataUtil;
import misstrace.Util.ImgUtil;
import misstrace.Util.JwtUtil;
import misstrace.Payload.Result;
import misstrace.Util.LocationUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
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
    public Result PostMiss(String text, MultipartFile img,Double  latitude, Double longitude, HttpServletRequest request) {
        if (img == null) return Result.failure(4002, "图片不能为空");
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
        String imgUrl = ImgUtil.uploadImg(img);
        if(imgUrl==null)return Result.failure(4001,"上传图片出错");
        missPost.setImg(imgUrl);
        missPost.setLongitude(longitude);
        missPost.setLatitude(latitude);
        missPost.setPostTime(DataUtil.getTime());//设定发帖时间

        missService.addMissPost(missPost);
        return Result.success(newtoken);
    }

    @PostMapping("/match/{id}")
    public Result PostMatch(@PathVariable("id") Integer missId, MultipartFile img, Double latitude, Double longitude, HttpServletRequest request) {
        if (img == null) return Result.failure(4002, "图片不能为空");
        String token = request.getHeader("token");

        User user = userService.getUserByToken(token);
        MissPost missPost = missService.findMissPostById(missId).get();
        if (user.getId().equals(missPost.getUser().getId())){
            return Result.failure(5001,"不能匹配自己的迷踪帖！");
        }
        if(missPost.getIsMatched()){
            return Result.failure(5002,"该迷踪帖已被匹配！");
        }
        if(missPost.getIsMatching()){
            return Result.failure(5003,"无法匹配。已经有人在匹配此帖了，看看其他迷踪帖吧");
        }


        MatchPost matchPost = new MatchPost();
        Integer matchId = matchService.getNewId();
        matchPost.setId(matchId);
        matchPost.setUser(user);
        matchPost.setMissPost(missPost);
        matchPost.setPostTime(DataUtil.getTime());//设定发帖时间
        matchPost.setLongitude(longitude);
        matchPost.setLatitude(latitude);
        Double distance = LocationUtil.getDistance(missPost.getLatitude(),missPost.getLongitude(),latitude,longitude);
        if(distance<=50){//距离小于50米，提交审核
            //这里把迷踪帖设为“有待审匹配帖”，从而不被搜索到
            missPost.setIsMatching(true);
            String imgUrl = ImgUtil.uploadImg(img);//位置没问题，再保存图片
            if(imgUrl==null)return Result.failure(4001,"上传图片出错");
            matchPost.setImg(imgUrl);
            missService.updateMissPost(missPost);
            matchService.addMatchPost(matchPost);
            return Result.success(JwtUtil.refreshToken(token));
        }else{//距离大于50米，直接判定为匹配失败

            return Result.failure(5004,"距离太远，直接判定为匹配失败");
        }

    }

    @PostMapping("/show")
    public Result showMiss(HttpServletRequest request) {
        String token = request.getHeader("token");
        Map<String, Object> info = JwtUtil.getInfo(token);
        User user = userService.findUserBySid((String) info.get("sid")).get();
        List dataList = missService.showPosts();
        Map list = new HashMap<>();
        list.put("postList",dataList);
        return Result.success(list, JwtUtil.refreshToken(token));
    }

}


