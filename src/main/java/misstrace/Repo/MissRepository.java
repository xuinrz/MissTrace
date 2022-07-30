package misstrace.Repo;

import misstrace.Entity.MissPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MissRepository extends JpaRepository<MissPost,Integer> {

    @Query("SELECT max(id) FROM MissPost ")
    Integer getMaxId();

    Optional<MissPost> findById(Integer id);

    @Query("FROM MissPost WHERE isPassed=true AND isMatching=false AND isMatched=false ORDER BY postTime DESC ")
    List<MissPost> findDisplayablePost();

    @Query("FROM MissPost WHERE isChecking=true ORDER BY postTime DESC ")
    List<MissPost>findCheckingPost();

    @Query("FROM MissPost WHERE user.id=?1 AND isChecking=false ")
    List<MissPost> getMissMessageByUserId(Integer userId);

    @Query("FROM MissPost WHERE user.id=?1 ")
    List<MissPost> findPostByUserId(Integer UserId);
}
