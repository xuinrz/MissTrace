package misstrace.Repo;

import misstrace.Entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsRepository extends JpaRepository<Goods,Integer> {

    @Query("SELECT max(id) FROM Goods ")
    Integer getMaxId();

    @Query("FROM Goods WHERE onSale=true ")
    List<Goods> findOnSaleGoods();
}
