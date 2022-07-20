package misstrace.Repo;

import misstrace.Entity.MatchPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<MatchPost, Integer> {
    @Query("SELECT max(id) FROM MatchPost ")
    Integer getMaxId();
}
