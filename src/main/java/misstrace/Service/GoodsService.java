package misstrace.Service;

import misstrace.Entity.Goods;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface GoodsService {
    List<Goods> showGoods();
    Integer getNewId();
    void createNewGoods(String description, MultipartFile img, Integer cost);
    void offSaleById(Integer id);
    Boolean buyGoodsByIdAndToken(Integer id,String token);
}
