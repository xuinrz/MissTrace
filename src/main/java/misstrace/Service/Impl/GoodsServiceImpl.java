package misstrace.Service.Impl;

import misstrace.Entity.Goods;
import misstrace.Entity.User;
import misstrace.Entity.UserProperty;
import misstrace.Repo.GoodsRepository;
import misstrace.Repo.UserPropertyRepository;
import misstrace.Service.GoodsService;
import misstrace.Service.UserPropertyService;
import misstrace.Service.UserService;
import misstrace.Util.ImgUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.*;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Resource
    GoodsRepository goodsRepository;
    @Resource
    UserService userService;
    @Resource
    UserPropertyRepository userPropertyRepository;
    @Resource
    UserPropertyService userPropertyService;

    @Override
    public List showGoods() {
        List<Goods> goodsList = goodsRepository.findOnSaleGoods();
        List dataList = new ArrayList();
        Goods goods;
        Map m;
        for (int i = 0; i < goodsList.size(); i++) {
            goods = goodsList.get(i);
            m = new HashMap();
            m.put("id", goods.getId());
            m.put("text", goods.getDescription());
            m.put("img", goods.getImg());
            m.put("cost", goods.getCost());
            dataList.add(m);
        }
        return dataList;
    }

    @Override
    public synchronized Integer getNewId() {
        Integer newId = goodsRepository.getMaxId();
        if (newId == null) return 1;
        else return newId + 1;
    }

    @Override
    public void createNewGoods(String description, MultipartFile img, Integer cost) {
        Goods goods = new Goods();
        goods.setId(getNewId());
        goods.setDescription(description);
        String imgPath = ImgUtil.uploadImg(img);
        goods.setImg(imgPath);
        goods.setCost(cost);
        goodsRepository.save(goods);
    }

    @Override
    public void offSaleById(Integer id) {
        Goods goods = goodsRepository.findById(id).get();
        goods.setOnSale(false);
        goodsRepository.save(goods);
    }

    @Override
    public Boolean buyGoodsByIdAndToken(Integer id, String token) {
        User user = userService.getUserByToken(token);
        Goods goods = goodsRepository.findById(id).get();
        Integer cost = goods.getCost();
        Integer coin = user.getCoin();
        if (coin >= cost) {
            user.costCoin(cost);
            userService.modifyUser(user);
            Optional<UserProperty> op = userPropertyRepository.findPropertyByUserIdAndGoodsId(user.getId(), goods.getId());
//            如果已经有记录，则加一
            if(op.isPresent()){
                UserProperty userProperty = op.get();
                Integer number = userProperty.getNumber();
                userProperty.setNumber(number+1);
                userPropertyRepository.save(userProperty);
            }else{//没有记录，则新建并设为1
                UserProperty userProperty= new UserProperty();
                userProperty.setUser(user);
                userProperty.setGoods(goods);
                userProperty.setId(userPropertyService.getNewId());//number默认为1，无需设置
                userPropertyRepository.save(userProperty);
            }
            return true;
        }else return false;
    }
}
