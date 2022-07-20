package misstrace.Repo;

import java.util.Optional;

import misstrace.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findBySid(String sid);

    Boolean existsBySid(String sid);

    @Query("SELECT max(id) FROM User")
    Integer getMaxId();
}
