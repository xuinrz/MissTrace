package misstrace.Service;

import misstrace.Entity.Goods;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GoodsService {
    List<Goods> showGoods();
    Integer getNewId();
    void createNewGoods(String description, String img, Integer cost);
}
