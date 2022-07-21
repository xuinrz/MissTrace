package misstrace.Repo;

import misstrace.Entity.UserProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPropertyRepository extends JpaRepository<UserProperty,Integer> {

    @Query("FROM UserProperty WHERE user.id=?1 AND goods.id=?2")
    Optional<UserProperty> findPropertyByUserIdAndGoodsId(Integer userId, Integer goodssId);

    @Query("SELECT max(id) FROM UserProperty ")
    Integer getMaxId();

    List<UserProperty> findByUser_Id(Integer userId);
}
