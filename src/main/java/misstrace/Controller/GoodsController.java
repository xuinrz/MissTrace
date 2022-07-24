package misstrace.Controller;

import misstrace.Entity.User;
import misstrace.Payload.Result;
import misstrace.Service.GoodsService;
import misstrace.Service.UserPropertyService;
import misstrace.Service.UserService;
import misstrace.Util.JwtUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/goods")
public class GoodsController {

    @Resource
    UserService userService;
    @Resource
    GoodsService goodsService;
    @Resource
    UserPropertyService userPropertyService;


    @PostMapping("/show")
    public Result showGoods(HttpServletRequest request) {
        List dataList = goodsService.showGoods();
        String token = request.getHeader("token");
        String newToken = JwtUtil.refreshToken(token);
        return Result.success(dataList,newToken);
    }
    @PostMapping("/add")
    public Result addGoods(String description, MultipartFile img, Integer cost, HttpServletRequest request) {
        String token = request.getHeader("token");
        if(userService.getUserByToken(token).getIsAdmin()) {
            goodsService.createNewGoods(description,img,cost);
            String newToken = JwtUtil.refreshToken(token);
            return Result.success(newToken);
        }else return Result.failure(-3,"添加失败，普通用户无权限添加商品");
    }

    @DeleteMapping("/delete/{id}")
    public Result addGoods(@PathVariable("id") Integer id, HttpServletRequest request) {
        String token = request.getHeader("token");
        if(userService.getUserByToken(token).getIsAdmin()) {
            goodsService.offSaleById(id);
            return Result.success(JwtUtil.refreshToken(token));
        }else return Result.failure(-3,"删除失败，普通用户无权限删除商品");
    }

    @PostMapping("/buy/{id}")
    public Result buyGoods(@PathVariable("id") Integer id, HttpServletRequest request){
        String token = request.getHeader("token");
        if(goodsService.buyGoodsByIdAndToken(id,token)){
            return Result.success(JwtUtil.refreshToken(token));
        }else return Result.failure(-1,"积分不足，购买失败！");
    }

    @PostMapping("/my")
    public Result showMyProperty(HttpServletRequest request){
        String token = request.getHeader("token");
        User user = userService.getUserByToken(token);
        List dataList = userPropertyService.myPropertyList(user.getId());
        return Result.success(dataList,JwtUtil.refreshToken(token));
    }
    @PostMapping("/download")
    public Result downloadUserProperty(HttpServletRequest request){
        String token = request.getHeader("token");
        if(userService.getUserByToken(token).getIsAdmin()) {


            String excel = userPropertyService.download();



            return Result.success(excel,JwtUtil.refreshToken(token));
        }else return Result.failure(-3,"下载失败，普通用户无权限");
    }

}