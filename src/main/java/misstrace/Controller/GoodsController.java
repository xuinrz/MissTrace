package misstrace.Controller;

import misstrace.Payload.Result;
import misstrace.Service.GoodsService;
import misstrace.Service.UserService;
import misstrace.Util.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/goods")
public class GoodsController {

    @Resource
    UserService userService;
    @Resource
    GoodsService goodsService;


    @PostMapping("/show")
    public Result showGoods(HttpServletRequest request) {
        List dataList = goodsService.showGoods();
        String token = request.getHeader("token");
        String newToken = JwtUtil.refreshToken(token);
        return Result.success(dataList,newToken);
    }
    @PostMapping("/add")
    public Result addGoods(String description, String img, Integer cost,HttpServletRequest request) {
        goodsService.createNewGoods(description,img,cost);
        String token = request.getHeader("token");
        String newToken = JwtUtil.refreshToken(token);
        return Result.success(newToken);
    }


}