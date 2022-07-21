package misstrace.Service.Impl;

import misstrace.Entity.Goods;
import misstrace.Repo.GoodsRepository;
import misstrace.Service.GoodsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Resource
    GoodsRepository goodsRepository;

    @Override
    public List showGoods() {
        List<Goods> goodsList = goodsRepository.findAll();
        List dataList = new ArrayList();
        Goods goods;
        Map m;
        for (int i = 0; i < goodsList.size(); i++) {
            goods = goodsList.get(i);
            m =new HashMap();
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
    public void createNewGoods(String description, String img, Integer cost) {
        Goods goods = new Goods();
        goods.setId(getNewId());
        goods.setDescription(description);
        goods.setImg(img);
        goods.setCost(cost);
        goodsRepository.save(goods);
    }
}
