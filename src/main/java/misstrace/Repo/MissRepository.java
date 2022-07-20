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

    @Query("FROM MissPost WHERE isPassed=TRUE ")
    List<MissPost> findPassedMissPosts();

}
